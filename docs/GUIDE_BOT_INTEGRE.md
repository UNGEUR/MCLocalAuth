# 🚀 Guide d'installation - Bot Discord Intégré

## ✨ Nouveau : Bot Discord Intégré au Plugin !

Le bot Discord est maintenant **intégré directement dans le plugin Minecraft**. Plus besoin de gérer deux applications séparées !

### 🎯 Avantages

- ✅ **Un seul processus** : Le bot démarre avec le serveur Minecraft
- ✅ **Communication directe** : Pas besoin d'API HTTP ni de port externe
- ✅ **Configuration simplifiée** : Tout dans le même fichier config.yml
- ✅ **Plus sécurisé** : Pas d'exposition de port réseau
- ✅ **Installation facile** : Un seul JAR à installer

## 📋 Prérequis

1. **Serveur Minecraft** avec Spigot/Paper 1.8+
2. **Token de bot Discord** (voir section ci-dessous)
3. **Maven** (pour compiler) ou utilisez le JAR précompilé

## 🔧 Installation

### 1. Récupérer le token Discord

1. Allez sur [Discord Developer Portal](https://discord.com/developers/applications)
2. Créez une nouvelle application
3. Allez dans "Bot" → "Add Bot"
4. Activez ces **Privileged Gateway Intents** :
   - ✅ SERVER MEMBERS INTENT
   - ✅ PRESENCE INTENT
5. Copiez le token du bot
6. Invitez le bot sur votre serveur avec les permissions "Administrator"

### 2. Compiler le plugin

```bash
cd plugin
mvn clean package
```

Le fichier `MCLocalAuth.jar` sera dans `plugin/target/`

### 3. Installer le plugin

1. Copiez `MCLocalAuth.jar` dans le dossier `plugins/` de votre serveur
2. Démarrez le serveur une première fois pour générer `config.yml`
3. Arrêtez le serveur

### 4. Configurer le plugin

Éditez `plugins/MCLocalAuth/config.yml` :

```yaml
# Configuration du bot Discord intégré (RECOMMANDÉ)
discord:
  enabled: true
  guild_id: "VOTRE_ID_SERVEUR_DISCORD"
  bot_token: "VOTRE_TOKEN_BOT_DISCORD"

# Configuration du code d'authentification
code:
  length: 6
  expireSeconds: 600
```

**Pour récupérer l'ID du serveur Discord :**
1. Activez le mode développeur dans Discord (Paramètres → Avancés → Mode développeur)
2. Clic droit sur votre serveur → "Copier l'identifiant"

### 5. Redémarrer le serveur

Démarrez votre serveur Minecraft. Vous devriez voir :

```
[MCLocalAuth] Démarrage du bot Discord intégré...
[MCLocalAuth] ✅ Bot Discord connecté : VotreBot#1234
[MCLocalAuth] ✅ Commandes Discord enregistrées sur le serveur
```

## 🎮 Utilisation

### Pour les joueurs :

1. **Se connecter** au serveur Minecraft
2. **Voir le code** dans le message de déconnexion (exemple: `123456`)
3. **Sur Discord** : `/auth MonPseudo 123456`
4. **Se reconnecter** au serveur → Accès autorisé !

### Commandes Discord disponibles :

- `/auth <pseudo> <code>` - S'authentifier avec le code Minecraft
- `/status` - Vérifier son statut d'authentification
- `/unlink` - Délier son compte (contact admin)

## 🔍 Dépannage

### Le bot ne démarre pas

```
❌ Vérifiez que le token est correct dans config.yml
❌ Vérifiez que les intents sont activés sur le Developer Portal
❌ Vérifiez les logs du serveur pour plus de détails
```

### Les commandes n'apparaissent pas sur Discord

```
⏳ Attendez jusqu'à 1 heure (si guild_id non défini)
✅ Définissez guild_id pour un enregistrement instantané
🔄 Redémarrez le serveur Minecraft
```

### Erreur "User not in Discord server"

```
❌ L'utilisateur doit être membre du serveur Discord
❌ Vérifiez que guild_id correspond au bon serveur
```

## 📊 Comparaison avec l'ancien système

| Fonctionnalité | Ancien (Bot séparé) | Nouveau (Bot intégré) |
|----------------|---------------------|----------------------|
| Processus | 2 (Bot + Plugin) | 1 (Plugin seul) |
| Configuration | 2 fichiers | 1 fichier |
| Port réseau | 8765 (exposé) | Aucun |
| Communication | HTTP API | Directe (Java) |
| Installation | Complexe | Simple |
| Sécurité | Moyenne | Élevée |

## 🆘 Support

Si vous rencontrez des problèmes :

1. Vérifiez les logs du serveur Minecraft
2. Vérifiez que le bot est en ligne sur Discord
3. Testez les commandes Discord manuellement
4. Vérifiez que les permissions du bot sont correctes

## 🎉 C'est tout !

Votre système d'authentification Discord est maintenant opérationnel. Le bot démarre automatiquement avec votre serveur Minecraft !
