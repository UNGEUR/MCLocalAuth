package fr.mclocalauth.mclocalauth;

import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.listeners.LoginListener;
import fr.mclocalauth.mclocalauth.storage.Storage;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class MCLocalAuthBungee extends Plugin {
    private Configuration config;
    private PendingManager pendingManager;
    private Storage storage;
    
    @Override
    public void onEnable() {
        // Charger la configuration
        loadConfig();
        
        // Initialiser les gestionnaires
        storage = new Storage(this);
        storage.load();
        
        int codeLength = config.getInt("code.length", 6);
        int expireSeconds = config.getInt("code.expireSeconds", 600);
        pendingManager = new PendingManager(this, codeLength, expireSeconds);
        
        // Enregistrer les listeners
        getProxy().getPluginManager().registerListener(this, new LoginListener(this, storage, pendingManager));
        
        // Discord non implémenté dans version BungeeCord
    }
    
    @Override
    public void onDisable() {
        if (storage != null) {
            storage.save();
        }
    }
    
    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Impossible de créer config.yml", e);
            }
        }
        
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Impossible de charger config.yml", e);
        }
    }
    
    public Configuration getConfiguration() {
        return config;
    }
    
    public Storage getStorage() {
        return storage;
    }
    
    public PendingManager getPendingManager() {
        return pendingManager;
    }
}
