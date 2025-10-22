package fr.mclocalauth.mclocalauth.discord;

import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private final MCLocalAuthPlugin plugin;
    private final PendingManager pendingManager;
    private final Storage storage;
    private JDA jda;
    private String guildId;
    
    public DiscordBot(MCLocalAuthPlugin plugin, PendingManager pendingManager, Storage storage) {
        this.plugin = plugin;
        this.pendingManager = pendingManager;
        this.storage = storage;
    }
    
    public void start(String token, String guildId) throws Exception {
        this.guildId = guildId;
        
        plugin.getLogger().info("Demarrage du bot Discord integre...");
        
        // Construction du bot avec les intents nÃ©cessaires
        JDABuilder builder = JDABuilder.createDefault(token)
            .enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES
            )
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setActivity(Activity.playing("Authentification Minecraft"));
        
        // Enregistrer le listener de commandes
        builder.addEventListeners(new DiscordCommandListener(plugin, pendingManager, storage, guildId));
        
        // Construire le bot de maniÃ¨re asynchrone
        jda = builder.build();
        
        // Attendre que le bot soit prÃªt dans un thread sÃ©parÃ©
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                jda.awaitReady();
                plugin.getLogger().info("[OK] Bot Discord connecte : " + jda.getSelfUser().getAsTag());
                
                // Enregistrer les commandes slash
                registerCommands();
                
            } catch (InterruptedException e) {
                plugin.getLogger().severe("[ERREUR] Erreur lors de la connexion du bot Discord: " + e.getMessage());
            }
        });
    }
    
    private void registerCommands() {
        plugin.getLogger().info("Suppression des anciennes commandes Discord...");
        
        if (guildId != null && !guildId.isEmpty()) {
            Guild guild = jda.getGuildById(guildId);
            if (guild != null) {
                // Supprimer toutes les anciennes commandes d'abord
                guild.updateCommands().queue(
                    success -> {
                        plugin.getLogger().info("[OK] Anciennes commandes supprimees");
                        // Puis enregistrer les nouvelles
                        guild.updateCommands().addCommands(
                            Commands.slash("auth", "Authentifier votre compte Minecraft")
                                .addOption(OptionType.STRING, "code", "Code d'authentification reçu en jeu", true),
                            Commands.slash("status", "Vérifier votre statut d'authentification"),
                            Commands.slash("unlink", "Délier votre compte Discord de Minecraft"),
                            Commands.slash("mctest", "Tester la connexion entre le bot et le plugin")
                        ).queue(
                            success2 -> plugin.getLogger().info("[OK] Nouvelles commandes Discord enregistrees sur le serveur"),
                            error2 -> plugin.getLogger().severe("[ERREUR] Erreur lors de l'enregistrement des commandes: " + error2.getMessage())
                        );
                    },
                    error -> plugin.getLogger().severe("[ERREUR] Erreur lors de la suppression des commandes: " + error.getMessage())
                );
            } else {
                plugin.getLogger().warning("[WARN] Serveur Discord introuvable avec l'ID: " + guildId);
            }
        } else {
            // Enregistrement global (plus lent)
            jda.updateCommands().queue(
                success -> {
                    plugin.getLogger().info("[OK] Anciennes commandes globales supprimees");
                    jda.updateCommands().addCommands(
                        Commands.slash("auth", "Authentifier votre compte Minecraft")
                            .addOption(OptionType.STRING, "code", "Code d'authentification reçu en jeu", true),
                        Commands.slash("status", "Vérifier votre statut d'authentification"),
                        Commands.slash("unlink", "Délier votre compte Discord de Minecraft"),
                        Commands.slash("mctest", "Tester la connexion entre le bot et le plugin")
                    ).queue(
                        success2 -> plugin.getLogger().info("[OK] Nouvelles commandes Discord enregistrees globalement"),
                        error2 -> plugin.getLogger().severe("[ERREUR] Erreur lors de l'enregistrement des commandes: " + error2.getMessage())
                    );
                },
                error -> plugin.getLogger().severe("[ERREUR] Erreur lors de la suppression des commandes: " + error.getMessage())
            );
        }
    }
    
    public void stop() {
        if (jda != null) {
            plugin.getLogger().info("Arret du bot Discord...");
            jda.shutdown();
        }
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    public boolean isUserInGuild(String userId) {
        if (jda == null || guildId == null || guildId.isEmpty()) {
            return true; // Si Discord desactive, autoriser
        }
        
        try {
            Guild guild = jda.getGuildById(guildId);
            if (guild == null) return false;
            
            return guild.getMemberById(userId) != null;
        } catch (Exception e) {
            plugin.getLogger().warning("Erreur lors de la verification Discord: " + e.getMessage());
            return false;
        }
    }
}
