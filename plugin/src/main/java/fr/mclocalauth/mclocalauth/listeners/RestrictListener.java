package fr.mclocalauth.mclocalauth.listeners;

import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import fr.mclocalauth.mclocalauth.auth.PendingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RestrictListener implements Listener {
    private final MCLocalAuthPlugin plugin;
    private final PendingManager pending;

    public RestrictListener(MCLocalAuthPlugin plugin, PendingManager pending) {
        this.plugin = plugin; this.pending = pending;
    }

    private boolean isBlocked(Player p) {
        return pending.isPending(p.getUniqueId());
    }

    private void sendBlocked(Player p) {
        String msg = plugin.getConfig().getString("messages.pendingBlocked", "Vous devez d'abord valider votre code.");
        p.sendMessage(plugin.getConfig().getString("messages.prefix", "[Auth] ") + msg);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!plugin.getConfig().getBoolean("restrictions.blockMovement", true)) return;
        Player p = e.getPlayer();
        if (isBlocked(p)) {
            if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
                e.setTo(e.getFrom());
            }
        }
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        if (!plugin.getConfig().getBoolean("restrictions.blockCommands", true)) return;
        Player p = e.getPlayer();
        if (isBlocked(p)) {
            // Liste d'exceptions Ã©ventuelles
            java.util.List<String> allow = plugin.getConfig().getStringList("restrictions.allowCommands");
            String cmd = e.getMessage().split(" ")[0].toLowerCase();
            if (!allow.contains(cmd)) {
                e.setCancelled(true);
                sendBlocked(p);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!plugin.getConfig().getBoolean("restrictions.blockInteractions", true)) return;
        Player p = e.getPlayer();
        if (isBlocked(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (isBlocked(p)) {
            e.setCancelled(true);
            sendBlocked(p);
        }
    }

    @EventHandler
    public void onInv(InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            if (isBlocked(p)) {
                e.setCancelled(true);
            }
        }
    }
}
