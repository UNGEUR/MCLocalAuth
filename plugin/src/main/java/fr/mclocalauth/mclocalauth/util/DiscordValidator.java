package fr.mclocalauth.mclocalauth.util;

import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordValidator {
    private final MCLocalAuthPlugin plugin;
    private final String botToken;
    private final String guildId;
    
    public DiscordValidator(MCLocalAuthPlugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.botToken = config.getString("discord.bot_token", "");
        this.guildId = config.getString("discord.guild_id", "");
    }
    
    /**
     * VÃ©rifie si un utilisateur Discord est membre du serveur
     * @param discordUserId L'ID Discord de l'utilisateur
     * @return true si l'utilisateur est membre du serveur, false sinon
     */
    public boolean isUserInGuild(String discordUserId) {
        if (botToken.isEmpty() || guildId.isEmpty()) {
            plugin.getLogger().warning("Configuration Discord incomplÃ¨te dans config.yml");
            return false;
        }
        
        try {
            String apiUrl = "https://discord.com/api/v10/guilds/" + guildId + "/members/" + discordUserId;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bot " + botToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == 200) {
                // L'utilisateur est membre du serveur
                return true;
            } else if (responseCode == 404) {
                // L'utilisateur n'est pas membre du serveur
                return false;
            } else {
                plugin.getLogger().warning("Erreur Discord API: " + responseCode);
                return false;
            }
            
        } catch (IOException e) {
            plugin.getLogger().severe("Erreur lors de la vÃ©rification Discord: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * VÃ©rifie si la configuration Discord est activÃ©e et correcte
     */
    public boolean isDiscordEnabled() {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("discord.enabled", false) && 
               !botToken.isEmpty() && 
               !guildId.isEmpty();
    }
}