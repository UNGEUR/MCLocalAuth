package fr.mclocalauth.mclocalauth.listeners;

import fr.mclocalauth.mclocalauth.MCLocalAuthBungee;
import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.storage.Storage;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class LoginListener implements Listener {
    private final MCLocalAuthBungee plugin;
    private final Storage storage;
    private final PendingManager pendingManager;
    
    public LoginListener(MCLocalAuthBungee plugin, Storage storage, PendingManager pendingManager) {
        this.plugin = plugin;
        this.storage = storage;
        this.pendingManager = pendingManager;
    }
    
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        String playerName = event.getPlayer().getName();
        String playerIp = event.getPlayer().getAddress().getAddress().getHostAddress();
        
        // VÃ©rifier si le joueur a une IP enregistrÃ©e
        String storedIp = storage.getIp(playerName);
        
        if (storedIp == null) {
            // PremiÃ¨re connexion - gÃ©nÃ©rer un code
            PendingManager.PendingEntry entry = pendingManager.createFor(event.getPlayer());
            
            // DÃ©connecter avec le message contenant le code
            plugin.getProxy().getScheduler().schedule(plugin, () -> {
                if (event.getPlayer().isConnected()) {
                    String kickMessage = createKickMessage(playerName, entry.code);
                    event.getPlayer().disconnect(new TextComponent(kickMessage));
                }
            }, 1, TimeUnit.SECONDS);
            
        } else if (!storedIp.equals(playerIp)) {
            // IP diffÃ©rente - vÃ©rifier si autorisÃ©e
            if (!storage.isIpAuthorized(playerName, playerIp)) {
                String message = plugin.getConfiguration().getString("messages.ip_not_authorized", "IP non autorisÃ©e")
                    .replace("%current_ip%", playerIp)
                    .replace("&", "Â§");
                event.getPlayer().disconnect(new TextComponent(message));
            }
        }
        // Sinon, IP reconnue - laisser passer
    }
    
    private String createKickMessage(String playerName, String code) {
        StringBuilder msg = new StringBuilder();
        
        for (String line : plugin.getConfiguration().getStringList("messages.kick_message")) {
            line = line.replace("%player%", playerName)
                      .replace("%code%", code)
                      .replace("&", "Â§");
            msg.append(line).append("\n");
        }
        
        return msg.toString();
    }
}
