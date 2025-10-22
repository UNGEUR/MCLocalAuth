# 🎮 MCLocalAuth v3.0 - Bot Discord Intégré

## 🚀 Nouvelle Version : Bot Discord Intégré !

**Le bot Discord est maintenant intégré directement dans le plugin Minecraft !**

Plus besoin de gérer deux applications séparées. Le bot démarre automatiquement avec votre serveur Minecraft.

## ✨ Fonctionnalités

### 🔐 Authentification Sécurisée
- ✅ Code d'authentification affiché dans le **message de déconnexion**
- ✅ Vérification Discord obligatoire (seuls les membres peuvent jouer)
- ✅ Système de protection IP avancé
- ✅ Codes temporaires (expiration configurable)

### 🤖 Bot Discord Intégré
- ✅ **Démarre avec le serveur** : Pas besoin de lancer le bot séparément
- ✅ **Communication directe** : Pas d'API HTTP externe
- ✅ **Configuration unique** : Tout dans config.yml
- ✅ **Ultra-sécurisé** : Aucun port réseau exposé

### 🎯 Commandes Discord
- `/auth <pseudo> <code>` - Authentifier son compte Minecraft
- `/status` - Vérifier son statut d'authentification
- `/unlink` - Délier son compte

### 🛡️ Sécurité
- Protection anti-usurpation d'identité
- Vérification membre Discord obligatoire
- Système IP multiples avec commandes admin
- Logs détaillés de toutes les tentatives

## 📦 Installation Rapide

### 1. Compiler le plugin

```bash
cd plugin
mvn clean package
```

Ou sur Windows, double-cliquez sur `COMPILER.bat`

### 2. Installer

```bash
# Copier le plugin
cp plugin/target/MCLocalAuth.jar /chemin/vers/serveur/plugins/

# Démarrer le serveur une fois pour générer la config
# Puis arrêter et éditer plugins/MCLocalAuth/config.yml
```

### 3. Configurer

Éditez `plugins/MCLocalAuth/config.yml` :

```yaml
discord:
  enabled: true
  guild_id: "VOTRE_ID_SERVEUR_DISCORD"
  bot_token: "VOTRE_TOKEN_BOT_DISCORD"
```

### 4. Démarrer

Lancez votre serveur Minecraft. Le bot Discord démarrera automatiquement !

## 📖 Documentation Complète

- **[GUIDE_BOT_INTEGRE.md](GUIDE_BOT_INTEGRE.md)** - Guide d'installation détaillé
- **[FONCTIONNALITES_FINALES.md](FONCTIONNALITES_FINALES.md)** - Liste complète des fonctionnalités
- **[GUIDE_IP_MULTIPLES.md](GUIDE_IP_MULTIPLES.md)** - Gestion des IP multiples

## 🎮 Utilisation

### Pour les Joueurs

1. **Connexion** au serveur Minecraft
2. **Message de déconnexion** avec le code d'authentification :
   ```
   === AUTHENTIFICATION DISCORD REQUISE ===
   
   Bienvenue PlayerName !
   Votre code d'authentification:
   » 123456 «
   
   Sur Discord, tapez: /auth PlayerName 123456
   ```
3. **Sur Discord** : `/auth PlayerName 123456`
4. **Reconnexion** → Accès autorisé !

### Pour les Admins

Commandes Minecraft :
- `/auth showips <joueur>` - Voir les IP autorisées
- `/auth addip <joueur> <ip>` - Ajouter une IP
- `/auth removeip <joueur> <ip>` - Supprimer une IP
- `/auth resetip <joueur>` - Réinitialiser les IP
- `/auth setip <joueur> <ip>` - Définir l'IP principale

## 🔧 Configuration

### Token Discord

1. Allez sur [Discord Developer Portal](https://discord.com/developers/applications)
2. Créez une application → Bot → Copiez le token
3. Activez **SERVER MEMBERS INTENT** dans les Privileged Gateway Intents
4. Invitez le bot avec permissions "Administrator"

### ID du Serveur Discord

1. Activez le mode développeur dans Discord
2. Clic droit sur votre serveur → "Copier l'identifiant"

## 📊 Avantages vs Ancien Système

| Aspect | Ancien (Bot séparé) | Nouveau (Bot intégré) |
|--------|---------------------|----------------------|
| **Installation** | Complexe (2 apps) | Simple (1 plugin) |
| **Configuration** | 2 fichiers | 1 fichier |
| **Sécurité** | Port 8765 exposé | Aucun port exposé |
| **Performance** | Communication HTTP | Communication directe |
| **Maintenance** | Gérer 2 processus | 1 seul processus |
| **Démarrage** | Manuel (bot + serveur) | Automatique |

## 🛠️ Technologies

- **Plugin Minecraft** : Spigot API 1.16.5 (compatible 1.8+)
- **Bot Discord** : JDA 5.0 (Java Discord API)
- **Build** : Maven
- **Langage** : Java 8+

## 📂 Structure du Projet

```
plugin/
├── src/main/java/fr/Author/mclocalauth/
│   ├── MCLocalAuthPlugin.java          # Classe principale
│   ├── discord/
│   │   ├── DiscordBot.java             # Bot Discord intégré
│   │   └── DiscordCommandListener.java # Commandes slash
│   ├── auth/
│   │   └── PendingManager.java         # Gestion des codes
│   ├── listeners/
│   │   ├── LoginListener.java          # Connexion joueurs
│   │   └── RestrictListener.java       # Restrictions
│   ├── storage/
│   │   └── Storage.java                # Stockage IP
│   └── commands/
│       └── AuthAdminCommand.java       # Commandes admin
├── src/main/resources/
│   ├── config.yml                      # Configuration
│   └── plugin.yml                      # Métadonnées
└── pom.xml                             # Build Maven
```

## 🔍 Dépannage

### Le bot ne démarre pas
```
❌ Token Discord invalide → Vérifiez config.yml
❌ Intents non activés → Activez SERVER MEMBERS INTENT
❌ Consultez les logs du serveur pour plus de détails
```

### Commandes Discord invisibles
```
⏳ Enregistrement global : jusqu'à 1h d'attente
✅ Définissez guild_id pour un enregistrement instantané
🔄 Redémarrez le serveur si nécessaire
```

### Erreur de connexion
```
❌ Vérifiez que le joueur est connecté au serveur
❌ Vérifiez que le code n'a pas expiré
❌ Vérifiez que l'utilisateur est membre du Discord
```

## 📝 Changelog

### v3.0 - Bot Discord Intégré
- ✨ Bot Discord intégré au plugin
- ✨ Suppression de l'API HTTP externe
- ✨ Configuration simplifiée
- ✨ Sécurité renforcée (aucun port exposé)
- ✨ Démarrage automatique avec le serveur

### v2.0 - Code dans le kick message
- ✨ Code affiché dans le message de déconnexion
- ✨ Vérification Discord obligatoire
- ✨ Système IP multiples avancé

## 📄 Licence

Ce projet est sous licence MIT. Vous êtes libre de l'utiliser et le modifier.

## 🤝 Support

Si vous rencontrez des problèmes :
1. Consultez les logs du serveur Minecraft
2. Vérifiez votre configuration Discord
3. Lisez le guide d'installation complet

---

**⭐ Le bot Discord démarre automatiquement avec votre serveur Minecraft !**
