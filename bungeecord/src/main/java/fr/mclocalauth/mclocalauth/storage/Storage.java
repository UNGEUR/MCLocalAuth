package fr.mclocalauth.mclocalauth.storage;

// import org.bukkit.Bukkit;
// import org.bukkit.OfflinePlayer;
// import org.bukkit.configuration.file.FileConfiguration;
// import org.bukkit.configuration.file.YamlConfiguration;
// import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Storage {
    private final Plugin plugin;
    private final File dataFile;
    private FileConfiguration data;

    public Storage(Plugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException ignored) {}
        }
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void save() {
        if (data == null) return;
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Erreur sauvegarde data.yml", e);
        }
    }

    private String path(UUID uuid) { return "users." + uuid.toString(); }

    /**
     * RÃƒÂ©cupÃƒÂ¨re la liste des IP autorisÃƒÂ©es pour un joueur
     */
    public List<String> getAuthorizedIps(UUID uuid) {
        List<String> ips = data.getStringList(path(uuid) + ".ips");
        return ips != null ? ips : new ArrayList<>();
    }

    /**
     * VÃƒÂ©rifie si une IP est autorisÃƒÂ©e pour un joueur
     */
    public boolean isIpAuthorized(UUID uuid, String ip) {
        List<String> authorizedIps = getAuthorizedIps(uuid);
        return authorizedIps.contains(ip);
    }

    /**
     * Ajoute une IP autorisÃƒÂ©e pour un joueur (premiÃƒÂ¨re connexion)
     */
    public void addAuthorizedIp(UUID uuid, String ip, String lastName) {
        String p = path(uuid);
        List<String> ips = getAuthorizedIps(uuid);
        
        if (!ips.contains(ip)) {
            ips.add(ip);
            data.set(p + ".ips", ips);
        }
        
        data.set(p + ".name", lastName);
        data.set(p + ".lastIp", ip);
        data.set(p + ".lastLogin", System.currentTimeMillis());
        save();
    }

    /**
     * Ajoute une IP supplÃƒÂ©mentaire pour un joueur (commande admin)
     */
    public boolean addIpForPlayer(UUID uuid, String ip) {
        String p = path(uuid);
        List<String> ips = getAuthorizedIps(uuid);
        
        if (ips.contains(ip)) {
            return false; // IP dÃƒÂ©jÃƒÂ  autorisÃƒÂ©e
        }
        
        ips.add(ip);
        data.set(p + ".ips", ips);
        save();
        return true;
    }

    /**
     * Supprime une IP autorisÃƒÂ©e pour un joueur
     */
    public boolean removeIpForPlayer(UUID uuid, String ip) {
        String p = path(uuid);
        List<String> ips = getAuthorizedIps(uuid);
        
        if (ips.size() <= 1) {
            return false; // Ne peut pas supprimer la derniÃƒÂ¨re IP
        }
        
        if (ips.remove(ip)) {
            data.set(p + ".ips", ips);
            save();
            return true;
        }
        return false;
    }

    /**
     * MÃƒÂ©thode de compatibilitÃƒÂ© - retourne la premiÃƒÂ¨re IP autorisÃƒÂ©e
     */
    public String getIp(UUID uuid) {
        List<String> ips = getAuthorizedIps(uuid);
        return ips.isEmpty() ? null : ips.get(0);
    }

    /**
     * MÃƒÂ©thode de compatibilitÃƒÂ© - ajoute une IP
     */
    public void setIp(UUID uuid, String ip, String lastName) {
        addAuthorizedIp(uuid, ip, lastName);
    }

    public void resetIp(UUID uuid) {
        data.set(path(uuid), null);
        save();
    }

    public String getName(UUID uuid) {
        return data.getString(path(uuid) + ".name");
    }

    public UUID findUuidByName(String name) {
        // Recherche directe parmi joueurs connus en local
        OfflinePlayer match = null;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(name)) {
                match = p; break;
            }
        }
        return match != null ? match.getUniqueId() : null;
    }
}
