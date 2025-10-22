# 📦 Plugins MCLocalAuth v1.1.0

Ce dossier contient les fichiers JAR compilés et prêts à l'emploi.

---

## 📁 Fichiers disponibles

### ✅ MCLocalAuth-Spigot-v1.1.0.jar
- **Type** : Plugin Spigot/Paper/Purpur
- **Taille** : ~10.68 MB
- **Compatible** : Minecraft 1.8 à 1.21+
- **Utilisation** : Serveur Minecraft standalone

**Installation :**
```bash
# Copier dans le dossier plugins/ de votre serveur
plugins/MCLocalAuth-Spigot-v1.1.0.jar
```

### ⏳ MCLocalAuth-Bungee-v1.1.0.jar
- **Statut** : ⚠️ En développement (code incomplet)
- **Type** : Plugin BungeeCord/Waterfall
- **Utilisation** : Réseau multi-serveurs avec proxy

**Note** : La version BungeeCord nécessite des classes supplémentaires pour être complète.  
**Alternative** : Installer la version Spigot sur chaque serveur backend avec base de données partagée.

Voir `/bungeecord/NOTE_BUNGEECORD.md` pour plus de détails.

---

## 🚀 Installation rapide

### Pour Spigot/Paper

1. **Télécharger** `MCLocalAuth-Spigot-v1.1.0.jar`

2. **Copier** dans `plugins/` de votre serveur Minecraft

3. **Démarrer** le serveur une fois (génère `config.yml`)

4. **Configurer** `plugins/MCLocalAuth/config.yml` :
   ```yaml
   discord:
     enabled: true
     guild_id: "VOTRE_ID_DISCORD"
     bot_token: "VOTRE_TOKEN_BOT"
   ```

5. **Redémarrer** le serveur

✅ Le bot Discord démarre automatiquement !

---

## 🔑 Configuration Discord

### Obtenir le token Discord

1. https://discord.com/developers/applications
2. Créer/Sélectionner application → Bot
3. Copier le token
4. **IMPORTANT** : Activer **SERVER MEMBERS INTENT**

### Obtenir l'ID du serveur Discord

1. Mode développeur Discord (Paramètres → Avancés)
2. Clic droit sur serveur → "Copier l'identifiant"

---

## 📊 Caractéristiques

### Inclus dans le JAR
- ✅ Bot Discord (JDA 5.0)
- ✅ Système d'authentification complet
- ✅ Protection IP avancée
- ✅ Commandes Discord slash
- ✅ Gestion multi-IP pour admins

### Configuration
- **Fichier** : `plugins/MCLocalAuth/config.yml`
- **Stockage** : `plugins/MCLocalAuth/data.yml`
- **Logs** : Logs du serveur Minecraft

---

## 🎯 Fonctionnalités

### Pour les joueurs
1. Se connecte au serveur
2. Reçoit un code dans le message de déconnexion
3. Tape `/auth pseudo code` sur Discord
4. Se reconnecte → Accès autorisé

### Pour les admins
```bash
/auth showips <joueur>         # Voir les IP
/auth addip <joueur> <ip>      # Ajouter une IP
/auth removeip <joueur> <ip>   # Supprimer une IP
/auth resetip <joueur>         # Réinitialiser
```

---

## 📖 Documentation

Pour plus d'informations, consultez :
- `README.md` - Documentation principale
- `QU_EST_CE_QUE_MCLOCALAUTH.md` - Explication détaillée
- `INSTALLATION_SERVEUR.md` - Guide installation serveur distant
- `RESUME_FINAL_V1.1.md` - Résumé complet

---

## ⚙️ Informations techniques

- **Version** : 1.1.0
- **Build** : Maven
- **Java** : 8+ (compilé en Java 8)
- **API Spigot** : 1.16.5 (compatible 1.8-1.21+)
- **API BungeeCord** : 1.21
- **Discord** : JDA 5.0.0-beta.20

---

## 🛡️ Sécurité

- Vérification membre Discord obligatoire
- Protection IP automatique
- Codes temporaires (expire après 10 min)
- Logs détaillés de toutes les tentatives
- Communication interne (aucun port exposé)

---

## 📝 Changelog

### v1.1.0 (21 octobre 2025)
- ✨ Bot Discord intégré au plugin
- ✨ Support Minecraft 1.21+
- ✨ Communication directe (sans HTTP externe)
- ✨ Version BungeeCord créée
- ✨ Anonymisation complète du code
- ✨ Documentation exhaustive

---

**MCLocalAuth v1.1.0** - Authentification Discord pour Minecraft  
*Sécurisez votre serveur, simplifiez la gestion, protégez vos joueurs.*
