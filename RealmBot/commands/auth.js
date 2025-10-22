const { SlashCommandBuilder, EmbedBuilder } = require('discord.js');
const UserManager = require('../utils/userManager');
const MinecraftApi = require('../utils/minecraftApi');

// Chargement de la configuration
let minecraftConfig;
try {
  minecraftConfig = require('../minecraft-config.json');
} catch (error) {
  console.error('❌ Fichier minecraft-config.json manquant ou invalide');
  process.exit(1);
}

const userManager = new UserManager();
const mcApi = new MinecraftApi(minecraftConfig.minecraft_server);

// Commande principale d'authentification
const authCommand = {
  data: new SlashCommandBuilder()
    .setName('auth')
    .setDescription('Authentification avec le serveur Minecraft')
    .addStringOption(option =>
      option.setName('pseudo')
        .setDescription('Votre pseudo Minecraft')
        .setRequired(true))
    .addStringOption(option =>
      option.setName('code')
        .setDescription('Code d\'authentification reçu en jeu')
        .setRequired(true)),

  async execute(interaction) {
    await interaction.deferReply({ ephemeral: true });

    const minecraftName = interaction.options.getString('pseudo');
    const code = interaction.options.getString('code');
    const discordId = interaction.user.id;

    // Validation basique du pseudo Minecraft
    if (!/^[a-zA-Z0-9_]{3,16}$/.test(minecraftName)) {
      return interaction.editReply({
        embeds: [createErrorEmbed('❌ Pseudo invalide', 'Le pseudo Minecraft doit contenir entre 3 et 16 caractères alphanumériques et underscores uniquement.')]
      });
    }

    // Validation basique du code
    if (!/^[0-9]{4,8}$/.test(code)) {
      return interaction.editReply({
        embeds: [createErrorEmbed('❌ Code invalide', 'Le code doit être composé de 4 à 8 chiffres.')]
      });
    }

    try {
      // Tentative de validation auprès du serveur Minecraft avec l'ID Discord
      const result = await mcApi.validateCode(minecraftName, code, discordId);

      if (result.success) {
        // Succès : lier le compte Discord au compte Minecraft
        userManager.linkUser(discordId, minecraftName);
        userManager.clearPendingAuth(discordId);

        // Optionnel : ajouter un rôle à l'utilisateur
        if (minecraftConfig.auth.role_name) {
          try {
            const role = interaction.guild.roles.cache.find(r => r.name === minecraftConfig.auth.role_name);
            if (role && interaction.member) {
              await interaction.member.roles.add(role);
            }
          } catch (roleError) {
            console.error('Erreur lors de l\'ajout du rôle:', roleError);
          }
        }

        return interaction.editReply({
          embeds: [createSuccessEmbed(
            '✅ Authentification réussie !',
            `Votre compte Discord a été lié avec succès au joueur **${minecraftName}**.\n\nVous êtes maintenant authentifié et avez accès aux fonctionnalités du serveur !`
          )]
        });
      } else {
        // Échec de la validation
        return interaction.editReply({
          embeds: [createErrorEmbed('❌ Authentification échouée', result.message)]
        });
      }
    } catch (error) {
      console.error('Erreur lors de l\'authentification:', error);
      return interaction.editReply({
        embeds: [createErrorEmbed(
          '❌ Erreur technique',
          'Une erreur est survenue lors de la validation. Réessayez dans quelques instants.'
        )]
      });
    }
  }
};

// Commande pour vérifier le statut d'authentification
const statusCommand = {
  data: new SlashCommandBuilder()
    .setName('status')
    .setDescription('Vérifier votre statut d\'authentification'),

  async execute(interaction) {
    const discordId = interaction.user.id;
    const linkedUser = userManager.getLinkedUser(discordId);

    if (linkedUser) {
      const embed = new EmbedBuilder()
        .setColor('#00FF00')
        .setTitle('✅ Compte authentifié')
        .addFields(
          { name: 'Pseudo Minecraft', value: linkedUser.minecraftName, inline: true },
          { name: 'Lié depuis', value: `<t:${Math.floor(new Date(linkedUser.linkedAt).getTime() / 1000)}:R>`, inline: true },
          { name: 'Dernière auth', value: `<t:${Math.floor(new Date(linkedUser.lastAuth).getTime() / 1000)}:R>`, inline: true }
        )
        .setFooter({ text: 'Utilisez /unlink pour délier votre compte' });

      return interaction.reply({ embeds: [embed], ephemeral: true });
    } else {
      const embed = new EmbedBuilder()
        .setColor('#FF0000')
        .setTitle('❌ Compte non authentifié')
        .setDescription('Votre compte Discord n\'est pas lié à un joueur Minecraft.\n\nPour vous authentifier :\n1. Connectez-vous au serveur Minecraft\n2. Utilisez la commande `/auth` en jeu\n3. Utilisez le code reçu avec `/auth <pseudo> <code>` ici')
        .setFooter({ text: 'Besoin d\'aide ? Contactez un administrateur' });

      return interaction.reply({ embeds: [embed], ephemeral: true });
    }
  }
};

// Commande pour délier un compte
const unlinkCommand = {
  data: new SlashCommandBuilder()
    .setName('unlink')
    .setDescription('Délier votre compte Discord de Minecraft'),

  async execute(interaction) {
    const discordId = interaction.user.id;
    
    if (userManager.unlinkUser(discordId)) {
      // Optionnel : retirer le rôle
      if (minecraftConfig.auth.role_name) {
        try {
          const role = interaction.guild.roles.cache.find(r => r.name === minecraftConfig.auth.role_name);
          if (role && interaction.member) {
            await interaction.member.roles.remove(role);
          }
        } catch (roleError) {
          console.error('Erreur lors du retrait du rôle:', roleError);
        }
      }

      return interaction.reply({
        embeds: [createSuccessEmbed('✅ Compte délié', 'Votre compte Discord a été délié avec succès.')]
      });
    } else {
      return interaction.reply({
        embeds: [createErrorEmbed('❌ Aucun compte lié', 'Votre compte Discord n\'est pas lié à un joueur Minecraft.')],
        ephemeral: true
      });
    }
  }
};

// Commande de test de connexion (pour les administrateurs)
const testCommand = {
  data: new SlashCommandBuilder()
    .setName('mctest')
    .setDescription('Tester la connexion au serveur Minecraft (admin seulement)'),

  async execute(interaction) {
    const cfg = (() => { try { return require('../token.json'); } catch { return {}; } })();
    const OWNER_ID = process.env.DISCORD_OWNER_ID || cfg.OWNER_ID;

    if (interaction.user.id !== OWNER_ID) {
      return interaction.reply({ 
        content: '🚫 Commande réservée aux administrateurs.', 
        ephemeral: true 
      });
    }

    await interaction.deferReply({ ephemeral: true });

    try {
      const result = await mcApi.testConnection();
      
      const embed = new EmbedBuilder()
        .setColor(result.success ? '#00FF00' : '#FF0000')
        .setTitle(result.success ? '✅ Test de connexion réussi' : '❌ Test de connexion échoué')
        .addFields(
          { name: 'Serveur', value: `${minecraftConfig.minecraft_server.host}:${minecraftConfig.minecraft_server.port}` },
          { name: 'Statut', value: result.message }
        )
        .setTimestamp();

      return interaction.editReply({ embeds: [embed] });
    } catch (error) {
      return interaction.editReply({
        embeds: [createErrorEmbed('❌ Erreur de test', error.message)]
      });
    }
  }
};

// Fonctions utilitaires pour les embeds
function createSuccessEmbed(title, description) {
  return new EmbedBuilder()
    .setColor('#00FF00')
    .setTitle(title)
    .setDescription(description)
    .setTimestamp();
}

function createErrorEmbed(title, description) {
  return new EmbedBuilder()
    .setColor('#FF0000')
    .setTitle(title)
    .setDescription(description)
    .setTimestamp();
}

// Nettoyage périodique des authentifications expirées
setInterval(() => {
  userManager.cleanupExpiredAuth();
}, 5 * 60 * 1000); // Toutes les 5 minutes

module.exports = [authCommand, statusCommand, unlinkCommand, testCommand];