const fs = require('node:fs');
const path = require('node:path');
const { Client, GatewayIntentBits, REST, Routes } = require('discord.js');

// Chargement configuration: variables d'environnement avec fallback sur token.json
const fileConfig = (() => { try { return require('./token.json'); } catch { return {}; } })();
const TOKEN = process.env.DISCORD_TOKEN || fileConfig.TOKEN;
const CLIENT_ID = process.env.DISCORD_CLIENT_ID || fileConfig.CLIENT_ID;
const GUILD_ID = process.env.DISCORD_GUILD_ID || fileConfig.GUILD_ID;
const OWNER_ID = process.env.DISCORD_OWNER_ID || fileConfig.OWNER_ID;
const ALLOW_DANGEROUS = (process.env.ALLOW_DANGEROUS === 'true');

if (!TOKEN || !CLIENT_ID) {
  console.error('❌ Configuration manquante: DISCORD_TOKEN et/ou DISCORD_CLIENT_ID.');
  process.exit(1);
}

const client = new Client({
  intents: [
    GatewayIntentBits.Guilds,
    GatewayIntentBits.GuildMessages,
    GatewayIntentBits.MessageContent,
    GatewayIntentBits.GuildVoiceStates // ✅ pour pouvoir gérer les salons vocaux
  ]
});

// --- Charger toutes les commandes depuis commands/ ---
const commandsPath = path.join(__dirname, 'commands');
const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));

const slashCommands = [];
const prefixCommands = [];

for (const file of commandFiles) {
  const cmd = require(path.join(commandsPath, file));
  if (Array.isArray(cmd)) {
    slashCommands.push(...cmd); // aplatit les tableaux
  } else if (cmd.data) {
    slashCommands.push(cmd);
  }
  if (cmd.prefix) prefixCommands.push(cmd);
}

// --- Préparer les données pour Discord ---
const commandData = slashCommands.map(cmd => cmd.data.toJSON());

// --- Enregistrement des commandes slash ---
const rest = new REST({ version: '10' }).setToken(TOKEN);

client.once('ready', async () => {
  console.log(`✅ Connecté en tant que ${client.user.tag}`);

  try {
    if (GUILD_ID) {
      console.log('⏳ Enregistrement des commandes sur la guilde...');
      await rest.put(Routes.applicationGuildCommands(CLIENT_ID, GUILD_ID), { body: commandData });
    } else {
      console.log('⏳ GUILD_ID absent → enregistrement GLOBAL des commandes (peut prendre jusqu\'à 1h).');
      await rest.put(Routes.applicationCommands(CLIENT_ID), { body: commandData });
    }
    console.log('✅ Commandes slash enregistrées !');
  } catch (error) {
    console.error(error);
  }
});

// --- Gestion des interactions slash ---
client.on('interactionCreate', async interaction => {
  if (!interaction.isChatInputCommand()) return;

  const command = slashCommands.find(cmd => cmd.data.name === interaction.commandName);
  if (!command) return;

  try {
    await command.execute(interaction);
  } catch (error) {
    console.error(error);
    await interaction.reply({ content: '❌ Une erreur est survenue.', flags: 64 }); // 64 = MessageFlags.Ephemeral
  }
});

// --- Gestion des commandes préfixe (-) ---
client.on('messageCreate', async message => {
  if (message.author.bot) return;

  for (const cmd of prefixCommands) {
    if (!message.content.startsWith(cmd.prefix)) continue;

    const args = message.content.slice(cmd.prefix.length).trim().split(/ +/);
    const commandName = args.shift().toLowerCase();

    if (commandName === cmd.name) {
      try {
        await cmd.execute(message, { OWNER_ID, GUILD_ID, ALLOW_DANGEROUS, args });
      } catch (error) {
        console.error(error);
        message.reply("❌ Une erreur est survenue lors de l'exécution de la commande.");
      }
      break;
    }
  }
});

// --- Connexion du bot ---
client.login(TOKEN);
