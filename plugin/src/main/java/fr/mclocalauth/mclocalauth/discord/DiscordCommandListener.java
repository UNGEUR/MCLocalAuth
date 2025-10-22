package fr.mclocalauth.mclocalauth.discord;

import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.auth.PendingManager.PendingEntry;
import fr.mclocalauth.mclocalauth.storage.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.time.Instant;
import java.util.UUID;

public class DiscordCommandListener extends ListenerAdapter {
    private final MCLocalAuthPlugin plugin;
    private final PendingManager pendingManager;
    private final Storage storage;
    private final String guildId;
    
    public DiscordCommandListener(MCLocalAuthPlugin plugin, PendingManager pendingManager, Storage storage, String guildId) {
        this.plugin = plugin;
        this.pendingManager = pendingManager;
        this.storage = storage;
        this.guildId = guildId;
    }
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        
        switch (commandName) {
            case "mclogin":
                handleAuth(event);
                break;
            case "status":
                handleStatus(event);
                break;
            case "unlink":
                handleUnlink(event);
                break;
            case "mctest":
                handleMcTest(event);
                break;
        }
    }
    
    private void handleAuth(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        
        String code = event.getOption("code").getAsString();
        String discordId = event.getUser().getId();
        
        // Validation du code
        if (!code.matches("^[0-9]{4,8}$")) {
            event.getHook().editOriginalEmbeds(
                createErrorEmbed("Code invalide", 
                    "Le code doit etre compose de 4 a 8 chiffres.")
            ).queue();
            return;
        }
        
        // Vérifier que l'utilisateur est membre du serveur Discord
        if (guildId != null && !guildId.isEmpty()) {
            Guild guild = event.getJDA().getGuildById(guildId);
            if (guild != null) {
                Member member = guild.getMemberById(discordId);
                if (member == null) {
                    event.getHook().editOriginalEmbeds(
                        createErrorEmbed("Acces refuse", 
                            "Vous devez etre membre du serveur Discord pour vous authentifier.")
                    ).queue();
                    return;
                }
            }
        }
        
        // Traiter dans le thread principal de Minecraft
        Bukkit.getScheduler().runTask(plugin, () -> {
            // Chercher dans tous les codes en attente
            PendingManager.PendingEntry entry = pendingManager.findByCode(code);
            
            if (entry == null) {
                event.getHook().editOriginalEmbeds(
                    createErrorEmbed("Code invalide", 
                        "Le code d'authentification est incorrect ou a expire.\n" +
                        "Assurez-vous d'avoir recu un code valide lors de la connexion.")
                ).queue();
                return;
            }
            
            // Code valide, consommer le code
            pendingManager.validate(entry.uuid, code, true);
            
            // Enregistrer l'IP du joueur
            storage.setIp(entry.uuid, entry.ip, null);
            
            // Log
            plugin.getLogger().info("Authentification reussie pour UUID " + entry.uuid + 
                                   " (Discord: " + event.getUser().getAsTag() + " - " + discordId + ")");
            
            // Reponse Discord
            event.getHook().editOriginalEmbeds(
                createSuccessEmbed("Authentification reussie !",
                    "Votre compte a ete authentifie avec succes !\n\n" +
                    "Vous pouvez maintenant vous reconnecter au serveur Minecraft.")
            ).queue();
        });
    }
    
    private void handleStatus(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        
        String discordId = event.getUser().getId();
        
        // Chercher si l'utilisateur a un compte lie
        // Note: Pour cela, il faudrait ajouter un systeme de liaison Discord dans Storage
        event.getHook().editOriginalEmbeds(
            createInfoEmbed("Statut d'authentification",
                "Pour vous authentifier :\n" +
                "1. Connectez-vous au serveur Minecraft\n" +
                "2. Notez le code affiche dans le message de deconnexion\n" +
                "3. Utilisez `/mclogin <code>` sur Discord")
        ).queue();
    }
    
    private void handleUnlink(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        
        String executorDiscordId = event.getUser().getId();
        String adminDiscordId = plugin.getConfig().getString("admin.discord_id", "");
        
        // Verifier si l'utilisateur est admin
        if (!executorDiscordId.equals(adminDiscordId) || adminDiscordId.isEmpty()) {
            event.getHook().editOriginalEmbeds(
                createErrorEmbed("Acces refuse",
                    "Seuls les administrateurs peuvent utiliser cette commande.")
            ).queue();
            return;
        }
        
        String targetDiscordId = event.getOption("discord_id").getAsString();
        
        // TODO: Implementation complete necessiterait un systeme de liaison Discord dans Storage
        // Pour l'instant on affiche juste le target
        event.getHook().editOriginalEmbeds(
            createInfoEmbed("Fonction en developpement",
                "La fonction /unlink pour le Discord ID: " + targetDiscordId + " sera implementee prochainement.\n" +
                "Actuellement, utilisez /auth resetip <joueur> en jeu.")
        ).queue();
        
        plugin.getLogger().info("[ADMIN] " + event.getUser().getAsTag() + " a tente de delier le Discord ID: " + targetDiscordId);
    }
    
    private MessageEmbed createSuccessEmbed(String title, String description) {
        return new EmbedBuilder()
            .setColor(Color.GREEN)
            .setTitle(title)
            .setDescription(description)
            .setTimestamp(Instant.now())
            .build();
    }
    
    private MessageEmbed createErrorEmbed(String title, String description) {
        return new EmbedBuilder()
            .setColor(Color.RED)
            .setTitle(title)
            .setDescription(description)
            .setTimestamp(Instant.now())
            .build();
    }
    
    private MessageEmbed createInfoEmbed(String title, String description) {
        return new EmbedBuilder()
            .setColor(Color.CYAN)
            .setTitle(title)
            .setDescription(description)
            .setTimestamp(Instant.now())
            .build();
    }
    
    private void handleMcTest(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        
        // Tester la connexion entre le bot et le plugin
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                int onlinePlayers = Bukkit.getOnlinePlayers().size();
                int pendingAuths = 0;
                
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (pendingManager.isPending(player.getUniqueId())) {
                        pendingAuths++;
                    }
                }
                
                String serverVersion = Bukkit.getVersion();
                String pluginVersion = plugin.getDescription().getVersion();
                
                event.getHook().editOriginalEmbeds(
                    new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle("Connexion Bot <-> Plugin Operationnelle")
                        .setDescription("Le bot Discord et le plugin Minecraft communiquent correctement !")
                        .addField("Statut", "**CONNECTE**", true)
                        .addField("Serveur", serverVersion, true)
                        .addField("Plugin", "v" + pluginVersion, true)
                        .addField("Joueurs en ligne", String.valueOf(onlinePlayers), true)
                        .addField("Authentifications en attente", String.valueOf(pendingAuths), true)
                        .addField("Bot Discord", "Fonctionnel", true)
                        .setTimestamp(Instant.now())
                        .setFooter("MCLocalAuth", null)
                        .build()
                ).queue();
                
                plugin.getLogger().info("✅ Test Discord réussi par " + event.getUser().getAsTag());
                
            } catch (Exception e) {
                event.getHook().editOriginalEmbeds(
                    createErrorEmbed("Erreur de connexion",
                        "Une erreur s'est produite lors du test : " + e.getMessage())
                ).queue();
                
                plugin.getLogger().warning("Erreur lors du test Discord : " + e.getMessage());
            }
        });
    }
}
