# RealmBot - Authentification Minecraft + Discord v2.0

Bot Discord intégré avec le plugin MCLocalAuth pour l'authentification des joueurs Minecraft.

## 🆕 Nouvelles fonctionnalités v2.0
- **Vérification obligatoire Discord** : Seuls les membres du serveur Discord peuvent jouer
- **Déconnexion automatique** : Les nouveaux joueurs sont déconnectés après l'affichage du code
- **Affichage amélioré** : Le code d'authentification est affiché clairement à l'écran

## 🚀 Configuration

### 1. Configuration du Plugin Minecraft

Le plugin MCLocalAuth doit être configuré pour accepter les connexions du bot. Modifiez le fichier `config.yml` du plugin :

```yaml
# Configuration de l'API HTTP
http:
  enabled: true
  host: "0.0.0.0"  # Important: 0.0.0.0 pour accepter les connexions externes
  port: 8765
  token: "votre-token-securise-ici"

# Configuration Discord - NOUVEAU !
discord:
  enabled: true
  guild_id: "123456789012345678"  # ID de votre serveur Discord
  bot_token: "votre_token_bot_discord"  # Token de votre bot Discord

# Configuration des codes
code:
  length: 6
  expireSeconds: 600  # 10 minutes
```

### 2. Configuration du Bot Discord

1. **Modifier `minecraft-config.json`** :
```json
{
  "minecraft_server": {
    "host": "IP_DU_SERVEUR_MINECRAFT",
    "port": 8765,
    "token": "votre-token-securise-ici",
    "protocol": "http"
  },
  "auth": {
    "role_name": "Authentifié",
    "channel_id": null,
    "timeout_minutes": 10
  }
}
```

2. **Modifier `token.json`** (déjà configuré) :
```json
{
  "TOKEN": "votre_token_discord_bot",
  "CLIENT_ID": "votre_client_id",
  "OWNER_ID": "votre_discord_id",
  "GUILD_ID": "id_de_votre_serveur_discord"
}
```

### 3. Configuration Réseau

**⚠️ Important pour les réseaux séparés :**

1. **Sur le serveur Minecraft**, ouvrez le port 8765 :
   - Windows : Pare-feu Windows avancé
   - Linux : `ufw allow 8765` ou `iptables`

2. **Router/Box Internet** (si nécessaire) :
   - Redirection de port 8765 vers le serveur Minecraft
   - Ou utilisation d'un VPN/tunnel

3. **Remplacez `127.0.0.1`** par l'IP publique ou locale du serveur Minecraft dans `minecraft-config.json`

## 📋 Commandes Discord

| Commande | Description |
|----------|-------------|
| `/auth <pseudo> <code>` | Authentification avec le code reçu en jeu |
| `/status` | Vérifier votre statut d'authentification |
| `/unlink` | Délier votre compte Discord de Minecraft |
| `/mctest` | Tester la connexion au serveur (admin uniquement) |

## 🎮 Utilisation

### Pour les joueurs :

1. **Être membre du serveur Discord** (obligatoire !)
2. **Se connecter au serveur Minecraft** → Recevoir le code d'authentification
3. **Être déconnecté automatiquement** après l'affichage du code
4. **Sur Discord, utiliser** `/auth pseudo 123456`
5. **Se reconnecter au serveur Minecraft** après validation
6. **Authentification réussie** → Rôle "Authentifié" attribué

### Pour les administrateurs :

- `/mctest` : Vérifier la connexion au serveur Minecraft
- Logs détaillés dans la console du bot
- Fichier `users.json` créé automatiquement pour stocker les liaisons

## 🔧 Dépannage

### Erreurs courantes :

1. **"Impossible de contacter le serveur"**
   - Vérifiez l'IP et le port dans `minecraft-config.json`
   - Vérifiez que le serveur Minecraft est démarré
   - Testez la connexion avec `/mctest`

2. **"Token d'authentification invalide"**
   - Les tokens dans `minecraft-config.json` et `config.yml` doivent être identiques

3. **"Joueur non connecté"**
   - Le joueur doit être en ligne sur le serveur Minecraft
   - Vérifiez l'orthographe du pseudo

4. **"Code invalide ou expiré"**
   - Le code expire après 10 minutes
   - Reconnectez-vous au serveur pour un nouveau code

5. **"Vous devez être membre du serveur Discord"**
   - Rejoignez d'abord le serveur Discord
   - Vérifiez la configuration `discord.guild_id` du plugin

6. **"Discord user ID required"**
   - Problème de communication bot-plugin
   - Vérifiez que le bot et plugin sont à jour

### Test de connexion :

```bash
# Test manuel de l'API (remplacez les valeurs)
curl "http://IP_SERVEUR:8765/mclogin?token=TOKEN&player=TestPlayer&code=123456"
```

## 📁 Structure des fichiers

```
RealmBot/
├── commands/
│   ├── auth.js          # Commandes d'authentification
│   └── commands.js      # Commandes de base (ping, stop)
├── utils/
│   ├── userManager.js   # Gestion des utilisateurs
│   └── minecraftApi.js  # API Minecraft
├── main.js              # Point d'entrée du bot
├── token.json           # Configuration Discord
├── minecraft-config.json # Configuration Minecraft
├── users.json           # Base de données des utilisateurs (auto-généré)
└── package.json
```

## 🔒 Sécurité

- **Token API** : Utilisez un token complexe et unique
- **Permissions Discord** : Le bot nécessite les permissions "Manage Roles"
- **Firewall** : Ne jamais exposer le port 8765 sur Internet sans protection
- **Logs** : Les tentatives d'authentification sont loggées

## 🆘 Support

En cas de problème :
1. Vérifiez les logs du bot Discord
2. Vérifiez les logs du serveur Minecraft
3. Utilisez `/mctest` pour diagnostiquer la connexion
4. Contactez l'administrateur du serveur