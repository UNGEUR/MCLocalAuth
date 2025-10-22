module.exports = {
  prefix: '-',
  name: 'clear_rôle',
  async execute(message, config) {
    const { OWNER_ID, GUILD_ID } = config;

    if (message.author.id !== OWNER_ID) return message.reply('🚫 Tu n’as pas la permission.');
    if (message.guild.id !== GUILD_ID) return;

    try {
      const roles = message.guild.roles.cache.filter(r => r.id !== message.guild.id); // exclut @everyone

      for (const role of roles.values()) {
        try {
          if (role.editable) await role.delete('Suppression via -clear_rôle par le propriétaire');
        } catch (err) {
          console.error(`Impossible de supprimer le rôle ${role.name}:`, err);
        }
      }

      await message.author.send('⚡ Tous les rôles supprimés (sauf @everyone).');

      if (message.deletable) await message.delete();
      console.log('✅ -clear_rôle exécuté par le propriétaire');
    } catch (error) {
      console.error(error);
      await message.author.send('❌ Une erreur est survenue lors de la suppression des rôles.');
    }
  }
};
