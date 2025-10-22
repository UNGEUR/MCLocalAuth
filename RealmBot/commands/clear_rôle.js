module.exports = {
  prefix: '-',
  name: 'clear_rôle',
  async execute(message, config) {
    const { OWNER_ID, GUILD_ID, ALLOW_DANGEROUS } = config;

    if (message.author.id !== OWNER_ID)
      return message.reply('🚫 Tu n’as pas la permission.');

    // Sécurité : désactivé par défaut et limité à une guilde autorisée.
    if (!ALLOW_DANGEROUS || (GUILD_ID && message.guild.id !== GUILD_ID)) {
      return message.reply('⛔ Commande désactivée pour des raisons de sécurité.');
    }

    try {
      // Supprime tous les rôles sauf @everyone
      const rolesToDelete = message.guild.roles.cache.filter(r => r.id !== message.guild.id);

      let success = 0;
      let failed = 0;

      for (const role of rolesToDelete.values()) {
        try {
          if (!role.editable) {
            failed++;
            continue;
          }
          await role.delete('Suppression via -clear_rôle par le propriétaire');
          success++;
        } catch (err) {
          console.error(`Impossible de supprimer ${role.name}:`, err);
          failed++;
        }
      }

      await message.author.send(
        `⚡ Suppression des rôles terminée.\n✅ ${success} supprimé(s)\n❌ ${failed} non supprimé(s)` +
        `\n⚠️ Si certains rôles n’ont pas été supprimés, place le rôle du bot au-dessus de tous les rôles et donne-lui Admin.`
      );

      if (message.deletable) await message.delete();
      console.log('✅ -clear_rôle exécuté par le propriétaire');
    } catch (err) {
      console.error(err);
      await message.author.send('❌ Une erreur est survenue lors de la suppression des rôles.');
    }
  }
};
