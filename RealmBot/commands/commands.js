const { SlashCommandBuilder } = require('discord.js');
const cfg = (() => { try { return require('../token.json'); } catch { return {}; } })();
const OWNER_ID = process.env.DISCORD_OWNER_ID || cfg.OWNER_ID;

const ping = {
  data: new SlashCommandBuilder()
    .setName('ping')
    .setDescription('Répond avec Pong!'),
  async execute(interaction) {
    await interaction.reply('🏓 Pong!');
  }
};

const stop = {
  data: new SlashCommandBuilder()
    .setName('stop')
    .setDescription('Éteint le bot (seul le propriétaire)'),
  async execute(interaction) {
    if (interaction.user.id !== OWNER_ID)
      return interaction.reply({ content: '🚫 Tu n’as pas la permission.', flags: 64 }); // ephemeral

    await interaction.reply({ content: '🛑 Bot éteint.', flags: 64 });
    process.exit(0);
  }
};

module.exports = [ping, stop];
