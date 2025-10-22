package fr.mclocalauth.mclocalauth;

import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.commands.AuthAdminCommand;
import fr.mclocalauth.mclocalauth.discord.DiscordBot;
import fr.mclocalauth.mclocalauth.http.HttpApi;
import fr.mclocalauth.mclocalauth.listeners.LoginListener;
import fr.mclocalauth.mclocalauth.listeners.RestrictListener;
import fr.mclocalauth.mclocalauth.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MCLocalAuthPlugin extends JavaPlugin {
    private Storage storage;
    private PendingManager pendingManager;
    private HttpApi httpApi;
    private DiscordBot discordBot;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.storage = new Storage(this);
        storage.load();

        FileConfiguration cfg = getConfig();
        int codeLength = Math.max(4, cfg.getInt("code.length", 6));
        int expireSec = Math.max(60, cfg.getInt("code.expireSeconds", 600));
        this.pendingManager = new PendingManager(this, codeLength, expireSec);

        // Listeners
        Bukkit.getPluginManager().registerEvents(new LoginListener(this, storage, pendingManager), this);
        Bukkit.getPluginManager().registerEvents(new RestrictListener(this, pendingManager), this);

        // Commande admin
        AuthAdminCommand adminCmd = new AuthAdminCommand(this, storage);
        getCommand("auth").setExecutor(adminCmd);
        getCommand("auth").setTabCompleter(adminCmd);

        // HTTP API pour intÃ©gration avec RealmBot (optionnel, dÃ©sactivÃ© si Discord intÃ©grÃ©)
        boolean discordEnabled = cfg.getBoolean("discord.enabled", false);
        
        if (discordEnabled) {
            // Bot Discord intÃ©grÃ© au plugin
            String discordToken = cfg.getString("discord.bot_token", "");
            String guildId = cfg.getString("discord.guild_id", "");
            
            if (discordToken.isEmpty() || discordToken.equals("YOUR_DISCORD_BOT_TOKEN")) {
                getLogger().warning("âš ï¸ Discord activÃ© mais token invalide. VÃ©rifiez config.yml");
            } else {
                this.discordBot = new DiscordBot(this, pendingManager, storage);
                try {
                    discordBot.start(discordToken, guildId);
                    getLogger().info("ðŸ‘ Bot Discord intÃ©grÃ© dÃ©marrÃ© !");
                } catch (Exception ex) {
                    getLogger().severe("âŒ Impossible de dÃ©marrer le bot Discord: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else if (cfg.getBoolean("http.enabled", true)) {
            // Fallback sur HTTP API si Discord dÃ©sactivÃ©
            String host = cfg.getString("http.host", "127.0.0.1");
            int port = cfg.getInt("http.port", 8765);
            String token = cfg.getString("http.token", "change-me");
            this.httpApi = new HttpApi(this, pendingManager, storage, host, port, token);
            try {
                httpApi.start();
                getLogger().info("API HTTP dÃ©marrÃ©e sur " + host + ":" + port);
            } catch (Exception ex) {
                getLogger().severe("Impossible de dÃ©marrer l'API HTTP: " + ex.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        if (discordBot != null) {
            discordBot.stop();
        }
        if (httpApi != null) {
            httpApi.stop();
        }
        storage.save();
    }

    public Storage getStorage() {
        return storage;
    }

    public PendingManager getPendingManager() {
        return pendingManager;
    }
}
