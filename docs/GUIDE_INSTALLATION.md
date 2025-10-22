# 🚀 Guide d'installation rapide - MCLocalAuth v2.0

## 📋 Résumé des modifications

### ✅ Nouvelles fonctionnalités implémentées :

1. **🔒 Vérification Discord obligatoire** : Seuls les membres du serveur Discord peuvent jouer
2. **📱 Déconnexion avec code** : Le code est affiché dans le message de déconnexion
3. **🎯 Affichage amélioré** : Message de kick formaté avec le code bien visible

### 📁 Fichiers modifiés :

**Plugin Minecraft :**
- `LoginListener.java` : Déconnexion automatique + vérification Discord
- `HttpApi.java` : Vérification de présence Discord
- `config.yml` : Nouvelle configuration Discord
- `DiscordValidator.java` : Nouvelle classe pour vérifier Discord

**Bot Discord :**
- `minecraftApi.js` : Envoi de l'ID Discord
- `auth.js` : Passage de l'ID utilisateur Discord
- `README.md` : Documentation mise à jour

## 🔧 Installation

### 1. Plugin Minecraft

1. **Compilez le plugin** (si Maven est installé) :
   ```bash
   cd plugin
   ./compile.bat   # Sur Windows
   mvn clean package   # Sur Linux/Mac
   ```

2. **Ou utilisez le JAR existant** : `plugin/target/MCLocalAuth.jar`

3. **Copiez le plugin** dans votre dossier `plugins/` du serveur

4. **Configurez le plugin** avec le nouveau `config.yml` :
   ```yaml
   discord:
     enabled: true
     guild_id: "VOTRE_ID_SERVEUR_DISCORD"
     bot_token: "VOTRE_TOKEN_BOT_DISCORD"
   ```

### 2. Bot Discord

1. **Les fichiers sont déjà modifiés** dans le dossier `RealmBot/`

2. **Configurez `minecraft-config.json`** :
   ```json
   {
     "minecraft_server": {
       "host": "IP_DE_VOTRE_SERVEUR_MC",
       "port": 8765,
       "token": "MEME_TOKEN_QUE_CONFIG_PLUGIN"
     }
   }
   ```

3. **Redémarrez le bot Discord**

## 🎮 Nouveau processus d'authentification

### Pour le joueur :
1. 🎮 **Connexion au serveur Minecraft**
2. 📺 **Code affiché dans le message de déconnexion** (exemple: `123456`)
3. ⚡ **Déconnexion automatique** après 1 seconde
4. 💬 **Sur Discord** : `/auth MonPseudo 123456`
5. ✅ **Validation** (seulement si membre du Discord)
6. 🔄 **Reconnexion au serveur Minecraft**
7. 🎉 **Accès autorisé !**

### Messages d'erreur :
- ❌ `"Vous devez être membre du serveur Discord"` : Rejoindre le Discord
- ❌ `"Discord user ID required"` : Problème de config bot/plugin
- ❌ `"User not in Discord server"` : Utilisateur banni/absent du Discord

## 🔐 Configuration Discord

### Récupérer l'ID du serveur Discord :
1. Activez le mode développeur dans Discord
2. Clic droit sur votre serveur → "Copier l'identifiant"
3. Collez dans `guild_id`

### Token du bot Discord :
1. Utilisez le même token que dans `token.json`
2. Le bot doit avoir les permissions "View Server Members"

## 🧪 Test de l'installation

1. **Testez la connexion** : `/mctest` sur Discord (admin uniquement)
2. **Testez l'authentification** : Connectez-vous avec un compte non-authentifié
3. **Vérifiez les logs** : Console du serveur MC + console du bot

## ⚠️ Points importants

- **Le bot Discord doit redémarrer** après modification des fichiers
- **Le serveur Minecraft doit redémarrer** après installation du plugin
- **Les deux tokens** (plugin et bot) doivent être identiques
- **Le port 8765** doit être ouvert sur le serveur Minecraft
- **Le bot Discord** doit avoir les permissions pour voir les membres

## 🆘 Dépannage

Si quelque chose ne fonctionne pas :
1. Vérifiez les logs du serveur Minecraft
2. Vérifiez la console du bot Discord
3. Utilisez `/mctest` pour tester la connexion
4. Vérifiez que les tokens correspondent
5. Vérifiez que le port 8765 est accessible

---

✅ **Installation terminée !** Votre serveur dispose maintenant d'une authentification Discord renforcée avec déconnexion automatique et vérification de membre obligatoire.