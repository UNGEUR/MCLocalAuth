const { SlashCommandBuilder } = require('discord.js');
const path = require('node:path');
const { spawn } = require('node:child_process');

const cfg = (() => { try { return require('../token.json'); } catch { return {}; } })();
const OWNER_ID = process.env.DISCORD_OWNER_ID || cfg.OWNER_ID;

module.exports = {
  data: new SlashCommandBuilder()
    .setName('restart')
    .setDescription('Redémarre le bot (réservé au propriétaire)')
  ,
  async execute(interaction) {
    try {
      if (interaction.user.id !== OWNER_ID) {
        return interaction.reply({ content: '🚫 Tu n’as pas la permission.', flags: 64 });
      }

      // Réponse éphémère avant de redémarrer
      await interaction.reply({ content: '♻️ Redémarrage en cours...', flags: 64 });

      // Lance un nouveau processus détaché, puis termine l’actuel
      const scriptPath = path.join(__dirname, '..', 'main.js');
      const child = spawn(process.execPath, [scriptPath], {
        detached: true,
        stdio: 'ignore',
        env: process.env,
      });
      child.unref();

      setTimeout(() => process.exit(0), 500);
    } catch (err) {
      console.error(err);
      try {
        await interaction.reply({ content: '❌ Erreur lors du redémarrage.', flags: 64 });
      } catch (_) {}
    }
  }
};
