const { PermissionFlagsBits, ChannelType } = require('discord.js');

module.exports = {
  prefix: '-',
  name: 'all',
  description: 'Supprime tous les salons et catégories du serveur',

  async execute(message, { OWNER_ID }) {
    const dm = async (content) => {
      try { await message.author.send(content); }
      catch (_) { try { await message.reply(content); } catch (_) {} }
    };

    const guild = message.guild;
    if (!guild) return;

    // Autoriser seulement le propriétaire du bot OU les membres avec Gérer les salons
    const isOwner = message.author.id === OWNER_ID;
    const hasManage = message.member?.permissions?.has(PermissionFlagsBits.ManageChannels);
    if (!isOwner && !hasManage) {
      return dm("🚫 Tu n’as pas la permission d’utiliser cette commande.");
    }

    // Vérifier les permissions du bot
    const me = guild.members.me;
    if (!me || !me.permissions.has(PermissionFlagsBits.ManageChannels)) {
      return dm('❌ Le bot n’a pas la permission "Gérer les salons".');
    }

    try {
      let count = 0;

      // ⚠️ On supprime d'abord les salons (pour vider les catégories), puis les catégories
      for (const channel of guild.channels.cache.values()) {
        if (
          channel.type === ChannelType.GuildText ||
          channel.type === ChannelType.GuildVoice ||
          channel.type === ChannelType.GuildAnnouncement ||
          channel.type === ChannelType.GuildStageVoice ||
          channel.type === ChannelType.GuildForum ||
          channel.type === ChannelType.GuildMedia
        ) {
          if (channel.deletable) {
            await channel.delete('Suppression via -all');
            count++;
          }
        }
      }

      // Ensuite on supprime les catégories
      for (const category of guild.channels.cache.values()) {
        if (category.type === ChannelType.GuildCategory && category.deletable) {
          await category.delete('Suppression via -all');
          count++;
        }
      }

      return dm(`✅ Tous les salons et catégories supprimés (${count} au total).`);
    } catch (err) {
      console.error(err);
      return dm('❌ Erreur lors de la suppression des salons et catégories.');
    }
  }
};
