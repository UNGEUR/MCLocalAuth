package fr.mclocalauth.mclocalauth.commands;

import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import fr.mclocalauth.mclocalauth.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
// Removed JetBrains annotations import

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AuthAdminCommand implements CommandExecutor, TabCompleter {
    private final MCLocalAuthPlugin plugin;
    private final Storage storage;

    public AuthAdminCommand(MCLocalAuthPlugin plugin, Storage storage) {
        this.plugin = plugin; this.storage = storage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mclocalauth.admin")) {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§6==== Commandes MCLocalAuth ====");
            sender.sendMessage("§e/auth showips <player> §7- Afficher les IP autorisees");
            sender.sendMessage("§e/auth addip <player> <ip> §7- Ajouter une IP autorisee");
            sender.sendMessage("§e/auth removeip <player> <ip> §7- Supprimer une IP");
            sender.sendMessage("§e/auth resetip <player> §7- Reinitialiser toutes les IP");
            sender.sendMessage("§e/auth setip <player> <ip> §7- Definir IP principale");
            return true;
        }
        String sub = args[0].toLowerCase();
        
        if (sub.equals("showips")) {
            if (args.length < 2) { sender.sendMessage("Â§cUsage: /auth showips <player>"); return true; }
            return handleShowIps(sender, args[1]);
            
        } else if (sub.equals("addip")) {
            if (args.length < 3) { sender.sendMessage("Â§cUsage: /auth addip <player> <ip>"); return true; }
            return handleAddIp(sender, args[1], args[2]);
            
        } else if (sub.equals("removeip")) {
            if (args.length < 3) { sender.sendMessage("Â§cUsage: /auth removeip <player> <ip>"); return true; }
            return handleRemoveIp(sender, args[1], args[2]);
            
        } else if (sub.equals("resetip")) {
            if (args.length < 2) { sender.sendMessage("Â§cUsage: /auth resetip <player>"); return true; }
            return handleResetIp(sender, args[1]);
            
        } else if (sub.equals("setip")) {
            if (args.length < 3) { sender.sendMessage("Â§cUsage: /auth setip <player> <ip>"); return true; }
            return handleSetIp(sender, args[1], args[2]);
        }
        sender.sendMessage("Sous-commande inconnue.");
        return true;
    }

    private String msg(String path) {
        return plugin.getConfig().getString("messages." + path, "");
    }
    
    private boolean handleShowIps(CommandSender sender, String playerName) {
        UUID uuid = storage.findUuidByName(playerName);
        if (uuid == null) {
            sender.sendMessage(msg("admin.notfound").replace("%player%", playerName));
            return true;
        }
        
        java.util.List<String> ips = storage.getAuthorizedIps(uuid);
        if (ips.isEmpty()) {
            sender.sendMessage(msg("admin.noip").replace("%player%", playerName));
        } else {
            sender.sendMessage("§6IP autorisees pour §e" + playerName + "§6:");
            for (int i = 0; i < ips.size(); i++) {
                String status = (i == 0) ? " Â§a(principale)" : "";
                sender.sendMessage("Â§7" + (i + 1) + ". Â§f" + ips.get(i) + status);
            }
        }
        return true;
    }
    
    private boolean handleAddIp(CommandSender sender, String playerName, String ip) {
        UUID uuid = storage.findUuidByName(playerName);
        if (uuid == null) {
            sender.sendMessage(msg("admin.notfound").replace("%player%", playerName));
            return true;
        }
        
        // VÃ©rifier format IP
        if (!isValidIp(ip)) {
            sender.sendMessage("Â§cFormat d'IP invalide: " + ip);
            return true;
        }
        
        if (storage.addIpForPlayer(uuid, ip)) {
            sender.sendMessage("§aIP §f" + ip + "§a ajoutee pour §e" + playerName);
            plugin.getLogger().info(sender.getName() + " a ajoute l'IP " + ip + " pour " + playerName);
        } else {
            sender.sendMessage("§eIP §f" + ip + "§e deja autorisee pour §e" + playerName);
        }
        return true;
    }
    
    private boolean handleRemoveIp(CommandSender sender, String playerName, String ip) {
        UUID uuid = storage.findUuidByName(playerName);
        if (uuid == null) {
            sender.sendMessage(msg("admin.notfound").replace("%player%", playerName));
            return true;
        }
        
        if (storage.removeIpForPlayer(uuid, ip)) {
            sender.sendMessage("§aIP §f" + ip + "§a supprimee pour §e" + playerName);
            plugin.getLogger().info(sender.getName() + " a supprime l'IP " + ip + " pour " + playerName);
        } else {
            java.util.List<String> ips = storage.getAuthorizedIps(uuid);
            if (ips.size() <= 1) {
                sender.sendMessage("§cImpossible de supprimer la derniere IP de §e" + playerName);
            } else {
                sender.sendMessage("§cIP §f" + ip + "§c introuvable pour §e" + playerName);
            }
        }
        return true;
    }
    
    private boolean handleResetIp(CommandSender sender, String playerName) {
        UUID uuid = storage.findUuidByName(playerName);
        if (uuid == null) {
            sender.sendMessage(msg("admin.notfound").replace("%player%", playerName));
            return true;
        }
        
        storage.resetIp(uuid);
        sender.sendMessage("§aToutes les IP de §e" + playerName + "§a ont ete reinitialisees");
        plugin.getLogger().info(sender.getName() + " a reinitialise les IP de " + playerName);
        return true;
    }
    
    private boolean handleSetIp(CommandSender sender, String playerName, String ip) {
        UUID uuid = storage.findUuidByName(playerName);
        if (uuid == null) {
            sender.sendMessage(msg("admin.notfound").replace("%player%", playerName));
            return true;
        }
        
        if (!isValidIp(ip)) {
            sender.sendMessage("Â§cFormat d'IP invalide: " + ip);
            return true;
        }
        
        storage.setIp(uuid, ip, playerName);
        sender.sendMessage(msg("admin.setip").replace("%player%", playerName).replace("%ip%", ip));
        plugin.getLogger().info(sender.getName() + " a defini l'IP " + ip + " pour " + playerName);
        return true;
    }
    
    private boolean isValidIp(String ip) {
        // Validation IPv4 ou IPv6
        try {
            java.net.InetAddress.getByName(ip);
            return true;
        } catch (java.net.UnknownHostException e) {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("showips", "addip", "removeip", "resetip", "setip");
        }
        
        if (args.length == 2) {
            // Liste des joueurs
            List<String> names = new ArrayList<String>();
            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                if (p.getName() != null) names.add(p.getName());
            }
            return names;
        }
        
        if (args.length == 3 && args[0].equalsIgnoreCase("removeip")) {
            // Pour removeip, proposer les IP existantes du joueur
            String playerName = args[1];
            UUID uuid = storage.findUuidByName(playerName);
            if (uuid != null) {
                return storage.getAuthorizedIps(uuid);
            }
        }
        
        return new ArrayList<String>();
    }
}
