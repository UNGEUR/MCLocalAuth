const { SlashCommandBuilder } = require('discord.js');
const axios = require('axios');
const fs = require('fs');
const path = require('path');

// Charger la configuration Minecraft
const configPath = path.join(__dirname, '..', 'minecraft-config.json');
let config = {};
try {
  config = JSON.parse(fs.readFileSync(configPath, 'utf8'));
} catch (error) {
  console.error('Erreur lors du chargement de minecraft-config.json:', error);
}

module.exports = {
  data: new SlashCommandBuilder()
    .setName('mclogin')
    .setDescription('Authentification pour rejoindre le serveur Minecraft')
    .addStringOption(option =>
      option.setName('code')
        .setDescription('Code d\'authentification reçu dans le chat Minecraft')
        .setRequired(true)),

  async execute(interaction) {
    const code = interaction.options.getString('code');
    const discordId = interaction.user.id;
    const discordUsername = interaction.user.username;

    try {
      await interaction.deferReply({ ephemeral: true });

      if (!config.minecraft_server) {
        return await interaction.editReply({
          content: '❌ Configuration du serveur Minecraft manquante. Contactez un administrateur.'
        });
      }

      // URL de l'API du plugin MCLocalAuth
      const url = `${config.minecraft_server.protocol}://${config.minecraft_server.host}:${config.minecraft_server.port}/validate`;
      
      const response = await axios.post(url, {
        discordId: discordId,
        discordUsername: discordUsername,
        code: code
      }, {
        headers: {
          'Authorization': `Bearer ${config.minecraft_server.token}`,
          'Content-Type': 'application/json'
        },
        timeout: 5000
      });

      if (response.data.success) {
        // Ajouter le rôle d'authentification si configuré
        if (config.auth.role_name) {
          const guild = interaction.guild;
          const role = guild.roles.cache.find(r => r.name === config.auth.role_name);
          if (role) {
            const member = guild.members.cache.get(discordId);
            if (member && !member.roles.cache.has(role.id)) {
              await member.roles.add(role);
              console.log(`Rôle ${config.auth.role_name} ajouté à ${discordUsername}`);
            }
          }
        }

        await interaction.editReply({
          content: `✅ **Authentification réussie !**\n\n` +
                   `🎮 Votre compte Discord est maintenant lié à votre compte Minecraft.\n` +
                   `🔒 Votre adresse IP a été enregistrée et vous pouvez maintenant jouer librement.\n\n` +
                   `Bon jeu sur le serveur ! 🚀`
        });

      } else {
        await interaction.editReply({
          content: `❌ **Authentification échouée**\n\n` +
                   `${response.data.message || 'Code invalide ou expiré.'}\n\n` +
                   `💡 Assurez-vous de :\n` +
                   `• Utiliser le bon code affiché dans le chat Minecraft\n` +
                   `• Utiliser le code avant qu'il n'expire (${config.auth.timeout_minutes || 10} minutes)`
        });
      }

    } catch (error) {
      console.error('Erreur lors de l\'authentification:', error);
      
      let errorMessage = '❌ **Erreur de connexion au serveur**\n\n';
      
      if (error.code === 'ECONNREFUSED') {
        errorMessage += '🔌 Le serveur Minecraft semble être hors ligne.\n';
      } else if (error.code === 'ETIMEDOUT') {
        errorMessage += '⏱️ Délai d\'attente dépassé.\n';
      } else {
        errorMessage += '⚠️ Une erreur technique est survenue.\n';
      }
      
      errorMessage += '\n📞 Contactez un administrateur si le problème persiste.';

      await interaction.editReply({ content: errorMessage });
    }
  }
};