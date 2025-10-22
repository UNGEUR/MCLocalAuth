# 🛡️ MCLocalAuth

**Plugin d'authentification Minecraft avec bot Discord intégré**

[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.8--1.21+-green.svg)]()
[![Java](https://img.shields.io/badge/Java-8+-blue.svg)]()

---

## 🎯 Description

MCLocalAuth est un plugin de sécurité pour serveur Minecraft qui utilise Discord comme système d'authentification. Protégez votre serveur en limitant l'accès aux seuls membres de votre communauté Discord.

### 🎮 Principe

1. **Joueur se connecte** → Déconnecté immédiatement avec un code unique
2. **Sur Discord** → Tape `/auth <code>`
3. **Bot vérifie** → Est-il membre du Discord ?
4. **Si oui** → IP enregistrée, reconnexion autorisée ✅
5. **Sinon** → Bloqué ❌

---

## ✨ Fonctionnalités

### 🔐 Sécurité Triple Couche

- ✅ **Discord obligatoire** : Seuls les membres de ton Discord peuvent jouer
- ✅ **Code temporaire** : Chaque connexion génère un code unique qui expire
- ✅ **Protection IP** : Un pseudo = Une IP → Impossible d'usurper une identité

### 🤖 Bot Discord Intégré

- Démarre automatiquement avec le serveur
- Aucun bot externe à héberger
- Aucun port réseau à ouvrir
- Commandes slash natives Discord

### 📋 Commandes Discord

- `/auth <code>` - S'authentifier avec le code reçu en jeu
- `/mctest` - Tester la connexion bot ↔ plugin
- `/status` - Vérifier son statut d'authentification
- `/unlink` - Délier son compte (à venir)

### ⚙️ Commandes Admin (in-game)

- `/auth showips <joueur>` - Afficher les IP autorisées
- `/auth addip <joueur> <ip>` - Ajouter une IP
- `/auth removeip <joueur> <ip>` - Supprimer une IP
- `/auth resetip <joueur>` - Réinitialiser les IP
- `/auth setip <joueur> <ip>` - Définir l'IP principale

---

## 📦 Installation

### Prérequis

- Serveur Minecraft **1.8 à 1.21+** (Spigot, Paper, Purpur)
- Java **8** minimum
- Bot Discord configuré sur [Discord Developer Portal](https://discord.com/developers/applications)

### Étapes

1. **Télécharger** le dernier `.jar` depuis [Releases](../../releases)
2. **Placer** le fichier dans le dossier `plugins/` de votre serveur
3. **Démarrer** le serveur pour générer la configuration
4. **Configurer** le bot Discord dans `plugins/MCLocalAuth/config.yml` :

```yaml
discord:
  enabled: true
  bot_token: "TON_TOKEN_BOT_DISCORD"
  guild_id: "TON_ID_SERVEUR_DISCORD"
```

5. **Redémarrer** le serveur
6. **Tester** avec `/mctest` sur Discord

---

## 🔧 Configuration

### Discord Bot Setup

1. Aller sur [Discord Developer Portal](https://discord.com/developers/applications)
2. Créer une nouvelle application
3. Onglet **Bot** → Créer un bot
4. **Copier le token** (ne jamais le partager !)
5. Activer ces **Privileged Gateway Intents** :
   - ✅ Server Members Intent
   - ✅ Message Content Intent (optionnel)
6. Onglet **OAuth2 → URL Generator** :
   - Cocher `bot` et `applications.commands`
   - Permissions : `Administrator` (ou personnalisées)
7. **Inviter** le bot sur ton serveur avec l'URL générée

### Obtenir l'ID du serveur Discord

1. Activer le **Mode Développeur** dans Discord :
   - Paramètres → Avancés → Mode développeur
2. Clic droit sur ton serveur → **Copier l'identifiant du serveur**
3. Coller dans `guild_id` du `config.yml`

---

## 📊 Compatibilité

| Version Minecraft | Status |
|-------------------|--------|
| 1.8 - 1.12        | ✅ Compatible |
| 1.13 - 1.16       | ✅ Compatible |
| 1.17 - 1.19       | ✅ Compatible |
| 1.20+             | ✅ Compatible |

**Serveurs supportés :** Spigot, Paper, Purpur, Pufferfish

---

## 🐛 Signaler un bug

Si tu rencontres un problème :

1. Vérifie que tu utilises la dernière version
2. Consulte les [Issues existantes](../../issues)
3. Si le problème persiste, [ouvre une nouvelle issue](../../issues/new) avec :
   - Version du plugin
   - Version Minecraft
   - Logs du serveur
   - Description détaillée du problème

---

## 💡 Suggérer une fonctionnalité

Tu as une idée ? [Ouvre une issue](../../issues/new) avec le tag `enhancement` !

---

## 📜 Licence

Ce projet est sous **licence propriétaire**. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

**TL;DR:**
- ✅ Utilisation libre sur serveurs Minecraft
- ✅ Voir le code pour apprendre
- ❌ Pas de modification/redistribution
- ❌ Pas d'utilisation commerciale

---

## 🙏 Crédits

**Développeur :** [Ton Pseudo]  
**Librairies utilisées :**
- [JDA](https://github.com/discord-jda/JDA) - Java Discord API
- [Spigot API](https://www.spigotmc.org/) - Minecraft Server API

---

## 📞 Support

- **Discord :** [Lien serveur Discord]
- **Issues :** [GitHub Issues](../../issues)
- **Email :** [Ton email si tu veux]

---

<div align="center">

**⭐ Si ce plugin t'est utile, n'hésite pas à mettre une étoile !**

Made with ❤️ for the Minecraft community


</div>
