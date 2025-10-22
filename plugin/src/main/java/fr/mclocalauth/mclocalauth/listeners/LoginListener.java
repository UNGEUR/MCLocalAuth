package fr.mclocalauth.mclocalauth.listeners;

import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.storage.Storage;
import fr.mclocalauth.mclocalauth.util.DiscordValidator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class LoginListener implements Listener {
    private final MCLocalAuthPlugin plugin;
    private final Storage storage;
    private final PendingManager pending;
    private final DiscordValidator discordValidator;

    public LoginListener(MCLocalAuthPlugin plugin, Storage storage, PendingManager pending) {
        this.plugin = plugin; this.storage = storage; this.pending = pending;
        this.discordValidator = new DiscordValidator(plugin);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        java.util.List<String> authorizedIps = storage.getAuthorizedIps(uuid);
        
        if (!authorizedIps.isEmpty()) {
            String currentIp = e.getAddress().getHostAddress();
            
            // VÃ©rifier si l'IP actuelle est dans la liste des IP autorisÃ©es
            if (!storage.isIpAuthorized(uuid, currentIp)) {
                String msg = plugin.getConfig().getString("messages.ipNotAuthorized", 
                    "Â§cConnexion refusÃ©e: Votre IP n'est pas autorisÃ©e pour ce compte.\n\nÂ§7Votre IP: Â§f" + currentIp + 
                    "\n\nÂ§eContactez un administrateur si vous jouez depuis un nouvel endroit.");
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
                
                // Log de sÃ©curitÃ©
                plugin.getLogger().warning("Tentative de connexion avec IP non autorisÃ©e: " + 
                    e.getName() + "(" + uuid + ") depuis " + currentIp + 
                    ". IP autorisÃ©es: " + authorizedIps);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        java.util.List<String> authorizedIps = storage.getAuthorizedIps(uuid);
        if (authorizedIps.isEmpty()) {
            PendingManager.PendingEntry entry = pending.get(uuid);
            if (entry == null) {
                entry = pending.createFor(p);
            }
            
            // CrÃ©er le message de dÃ©connexion avec le code
            String kickMessage = createKickMessage(p, entry.code);
            
            // DÃ©connecter le joueur immÃ©diatement avec le code dans le message
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (p.isOnline()) {
                    p.kickPlayer(kickMessage);
                }
            }, 20L); // 1 seconde pour laisser le temps de charger
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Rien de spÃ©cial: on laisse la demande en attente tant qu'elle n'expire pas
    }

    private String createKickMessage(Player p, String code) {
        FileConfiguration cfg = plugin.getConfig();
        String prefix = cfg.getString("messages.prefix", "[Auth] ");
        StringBuilder message = new StringBuilder();
        
        // Construire le message de dÃ©connexion avec les lignes de configuration
        java.util.List<String> lines = cfg.getStringList("messages.firstJoin");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            line = line.replace("%player%", p.getName()).replace("%code%", code);
            message.append(line);
            if (i < lines.size() - 1) {
                message.append("\n"); // Nouvelle ligne entre chaque message
            }
        }
        
        return message.toString();
    }
}
