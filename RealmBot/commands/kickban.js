const { PermissionFlagsBits } = require('discord.js');

module.exports = {
  prefix: '-',
  name: 'kick',
  description: 'Expulse tous les membres du serveur (confirmation requise)',
  async execute(message, { OWNER_ID, args = [] }) {
    const dm = async (content) => {
      try { await message.author.send(content); }
      catch (_) { try { await message.reply(content); } catch (_) {} }
    };

    if (message.author.id !== OWNER_ID) {
      return dm('🚫 Tu n\'as pas la permission d\'utiliser cette commande.');
    }

    const guild = message.guild;
    if (!guild) return;

    const me = guild.members.me;
    if (!me || !me.permissions.has(PermissionFlagsBits.KickMembers)) {
      return dm('❌ Le bot n\'a pas la permission "Expulser des membres".');
    }

    // Récupérer tous les membres pour être sûr d'avoir le cache à jour
    await guild.members.fetch().catch(() => {});

    const ownerId = guild.ownerId;

    // Tous les membres sauf le propriétaire et le bot
    const candidates = guild.members.cache.filter(m => m.id !== ownerId && m.id !== me.id);

    if (candidates.size === 0) {
      return dm('ℹ️ Aucun membre à expulser.');
    }

    const toKick = candidates.filter(m => m.kickable);
    const nonKickable = candidates.filter(m => !toKick.has(m.id));

    // Confirmation requise via argument: -kickban confirm
    const confirmed = (args[0] && args[0].toLowerCase() === 'confirm');
    if (!confirmed) {
      const summary = [
        `🧾 Total membres: ${candidates.size}`,
        `• Expulsables: ${toKick.size}`,
        `• Non expulsables (propriétaire, rôle supérieur, permissions insuffisantes, etc.): ${nonKickable.size}`,
        '',
        'Pour confirmer, exécute: -kickban confirm'
      ].join('\n');
      return dm(summary);
    }

    let ok = 0, fail = 0;
    for (const member of toKick.values()) {
      try {
        await member.kick('Expulsion par -kickban (tous les membres)');
        ok++;
      } catch (err) {
        console.error(`Kick échoué pour ${member.user?.tag || member.id}`, err);
        fail++;
      }
    }

    const report = [
      '✅ Opération terminée.',
      `• Expulsés: ${ok}`,
      `• Échecs: ${fail}`,
      `• Non traités (non expulsables): ${nonKickable.size}`
    ].join('\n');

    return dm(report);
  }
};
