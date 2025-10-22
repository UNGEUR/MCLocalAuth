# 🚀 Installation sur le serveur 45.140.164.92:25606

## 📋 Résumé

Ce guide vous explique comment installer MCLocalAuth v3.0 avec le bot Discord **intégré** sur votre serveur Minecraft distant.

## ✨ Ce qui change

**AVANT** : Bot Discord séparé qui essayait de se connecter au plugin via HTTP
- ❌ Problème de connexion entre machines distantes
- ❌ Port 8765 à ouvrir et sécuriser
- ❌ Configuration complexe

**MAINTENANT** : Bot Discord intégré dans le plugin
- ✅ Un seul processus sur le serveur Minecraft
- ✅ Aucun port supplémentaire à ouvrir
- ✅ Configuration simple dans un seul fichier

## 🔧 Étapes d'Installation

### 1. Compiler le plugin sur votre PC

```powershell
# Ouvrir PowerShell dans le dossier du projet
cd "C:\Users\Author\Documents\MEGA\bot et plugin"

# Compiler
cd plugin
mvn clean package
```

Ou double-cliquez sur `COMPILER.bat`

Le fichier `MCLocalAuth.jar` sera créé dans `plugin\target\`

### 2. Transférer le plugin sur le serveur

Utilisez FTP/SFTP pour uploader le fichier sur votre serveur :

```bash
# Exemple avec scp (depuis votre PC)
scp plugin/target/MCLocalAuth.jar root@45.140.164.92:/chemin/vers/serveur/plugins/
```

Ou utilisez FileZilla/WinSCP pour transférer le fichier vers :
```
/chemin/vers/serveur/plugins/MCLocalAuth.jar
```

### 3. Première configuration

**Sur le serveur**, démarrez Minecraft une première fois pour générer la config :

```bash
# Le plugin va créer plugins/MCLocalAuth/config.yml
# Arrêtez le serveur après le démarrage
```

### 4. Configurer Discord

Éditez `plugins/MCLocalAuth/config.yml` sur le serveur :

```yaml
discord:
  enabled: true
  guild_id: "VOTRE_ID_SERVEUR_DISCORD"
  bot_token: "VOTRE_TOKEN_BOT"
```

**📌 Important** : 
- Utilisez le fichier `config-serveur-exemple.yml` fourni comme référence
- Le token Discord doit être celui de votre bot
- L'ID du serveur Discord est récupérable en mode développeur

### 5. Obtenir le token Discord

1. Allez sur [Discord Developer Portal](https://discord.com/developers/applications)
2. Sélectionnez/créez votre application
3. Section "Bot" → Copiez le token
4. **IMPORTANT** : Dans "Privileged Gateway Intents", activez :
   - ✅ **SERVER MEMBERS INTENT**
   - ✅ **PRESENCE INTENT**

### 6. Inviter le bot sur Discord

Générez un lien d'invitation :

```
https://discord.com/api/oauth2/authorize?client_id=VOTRE_CLIENT_ID&permissions=8&scope=bot%20applications.commands
```

Remplacez `VOTRE_CLIENT_ID` par l'ID de votre application Discord.

### 7. Redémarrer le serveur Minecraft

```bash
# Sur le serveur
./start.sh  # ou votre commande de démarrage
```

Vous devriez voir dans les logs :

```
[MCLocalAuth] Démarrage du bot Discord intégré...
[MCLocalAuth] ✅ Bot Discord connecté : VotreBot#1234
[MCLocalAuth] ✅ Commandes Discord enregistrées sur le serveur
```

## 🎮 Test de l'installation

### 1. Test de connexion Minecraft

1. Connectez-vous à `45.140.164.92:25606`
2. Vous devriez être déconnecté avec un message contenant un code
3. Notez le code (exemple: `123456`)

### 2. Test sur Discord

Sur votre serveur Discord, tapez :
```
/auth VotrePseudo 123456
```

Le bot devrait répondre avec un message de succès.

### 3. Test de reconnexion

Reconnectez-vous au serveur Minecraft → Vous êtes authentifié !

## 🔍 Vérifications

### Le bot Discord est-il en ligne ?

Sur Discord, vérifiez que votre bot apparaît en ligne avec le statut "Authentification Minecraft"

### Les commandes sont-elles disponibles ?

Tapez `/` sur Discord, vous devriez voir :
- `/auth`
- `/status`
- `/unlink`

### Les logs du serveur

```bash
# Sur le serveur, consultez les logs
tail -f logs/latest.log
```

Recherchez les lignes `[MCLocalAuth]` pour voir l'activité.

## 📊 Architecture Finale

```
┌─────────────────────────────────────┐
│   Serveur Minecraft                 │
│   45.140.164.92:25606              │
│                                     │
│  ┌──────────────────────────────┐  │
│  │  MCLocalAuth.jar             │  │
│  │                              │  │
│  │  ┌────────────────────────┐  │  │
│  │  │  Plugin Minecraft      │  │  │
│  │  └────────────────────────┘  │  │
│  │            ↕                 │  │
│  │  ┌────────────────────────┐  │  │
│  │  │  Bot Discord (JDA)     │  │  │
│  │  └────────────────────────┘  │  │
│  │                              │  │
│  └──────────────────────────────┘  │
│            ↕                        │
└─────────────────────────────────────┘
             ↕
    Discord API (Internet)
             ↕
┌─────────────────────────────────────┐
│   Serveur Discord                   │
│   Vos membres                       │
└─────────────────────────────────────┘
```

**Avantages** :
- ✅ Tout tourne sur le serveur Minecraft
- ✅ Aucune configuration réseau supplémentaire
- ✅ Communication interne ultra-rapide
- ✅ Un seul processus à surveiller

## 🛠️ Maintenance

### Mettre à jour le plugin

1. Compiler la nouvelle version sur votre PC
2. Arrêter le serveur Minecraft
3. Remplacer `MCLocalAuth.jar`
4. Redémarrer le serveur

### Changer le token Discord

1. Éditer `plugins/MCLocalAuth/config.yml`
2. Modifier `discord.bot_token`
3. Redémarrer le serveur

### Logs et débogage

```bash
# Voir les logs en temps réel
tail -f logs/latest.log | grep MCLocalAuth

# Voir les 100 dernières lignes
tail -n 100 logs/latest.log | grep MCLocalAuth
```

## ⚠️ Important : Ne pas faire

- ❌ **Ne lancez PAS** l'ancien bot Discord Node.js
- ❌ **N'ouvrez PAS** le port 8765 (HTTP API désactivé)
- ❌ **Ne modifiez PAS** `http.enabled: true` dans la config

## ✅ Checklist finale

- [ ] Plugin compilé et transféré sur le serveur
- [ ] Token Discord configuré dans config.yml
- [ ] ID du serveur Discord configuré
- [ ] SERVER MEMBERS INTENT activé sur Discord
- [ ] Bot invité sur le serveur Discord
- [ ] Serveur Minecraft redémarré
- [ ] Bot apparaît en ligne sur Discord
- [ ] Commandes `/auth`, `/status`, `/unlink` disponibles
- [ ] Test d'authentification réussi

## 🎉 Terminé !

Votre serveur Minecraft à `45.140.164.92:25606` est maintenant équipé d'un système d'authentification Discord complètement intégré !

Le bot Discord démarre automatiquement avec le serveur et ne nécessite aucune gestion séparée.
