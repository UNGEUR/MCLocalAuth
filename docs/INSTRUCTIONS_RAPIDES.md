# ⚡ Instructions Rapides - MCLocalAuth v1.1

## 📌 CE QUI A ÉTÉ FAIT

✅ **Bot Discord intégré** dans le plugin Minecraft  
✅ **Version 1.1.0** avec support Minecraft 1.21  
✅ **Version BungeeCord** créée  
✅ **Documentation complète** générée  

## 🎯 CE QU'IL FAUT FAIRE MAINTENANT

### Étape 1 : Installer un JDK

**Problème** : Tu as un JRE, mais Maven a besoin d'un JDK pour compiler.

**Solution la plus simple** :
1. Va sur https://adoptium.net/temurin/releases/?version=8
2. Télécharge "Windows x86" / "JDK" / ".msi installer"
3. Installe le fichier téléchargé
4. Vérifie avec : `javac -version`

### Étape 2 : Compiler les plugins

Une fois le JDK installé :

```powershell
# Se placer dans le dossier du projet
cd "C:\Users\Author\Documents\MEGA\bot et plugin"

# Compiler version Spigot/Paper
cd plugin
mvn clean package

# Compiler version BungeeCord
cd ..\bungeecord
mvn clean package
```

**Résultats** :
- `plugin\target\MCLocalAuth.jar` (pour Spigot/Paper)
- `bungeecord\target\MCLocalAuth-Bungee.jar` (pour BungeeCord)

### Étape 3 : Choisir ta version

**Tu as un serveur unique ?** → Utilise `MCLocalAuth.jar` (Spigot)

**Tu as plusieurs serveurs avec BungeeCord ?** → Utilise `MCLocalAuth-Bungee.jar`

### Étape 4 : Installer sur le serveur 45.140.164.92

1. **Uploader le JAR** sur ton serveur :
   ```bash
   # Via FTP/SFTP vers :
   /chemin/vers/serveur/plugins/MCLocalAuth.jar
   ```

2. **Démarrer le serveur** une première fois pour générer la config

3. **Configurer** `plugins/MCLocalAuth/config.yml` :
   ```yaml
   discord:
     enabled: true
     guild_id: "TON_ID_SERVEUR_DISCORD"
     bot_token: "TON_TOKEN_BOT"
   ```

4. **Redémarrer le serveur**

Le bot Discord démarrera automatiquement ! 🎉

## 📂 FICHIERS IMPORTANTS

| Fichier | Description |
|---------|-------------|
| **README.md** | Documentation principale |
| **GUIDE_COMPILATION.md** | Comment compiler (détails JDK) |
| **INSTALLATION_SERVEUR.md** | Installation sur 45.140.164.92 |
| **MODIFICATIONS_V1.1.md** | Résumé de tout ce qui a été fait |

## 🔑 Configuration Discord

### Obtenir le token :
1. https://discord.com/developers/applications
2. Sélectionne/crée ton application
3. Section "Bot" → Copie le token
4. **IMPORTANT** : Active "SERVER MEMBERS INTENT"

### Obtenir l'ID du serveur :
1. Mode développeur Discord (Paramètres → Avancés)
2. Clic droit sur ton serveur → "Copier l'identifiant"

## ❗ IMPORTANT

⚠️ **N'utilise PLUS le bot Node.js** (`RealmBot/main.js`)  
⚠️ **Le bot est maintenant intégré** dans le plugin Minecraft  
⚠️ **Plus besoin d'ouvrir le port 8765**  

## 🎮 Test rapide

1. Lance ton serveur Minecraft
2. Connecte-toi au serveur
3. Tu seras déconnecté avec un code (ex: `123456`)
4. Sur Discord : `/auth TonPseudo 123456`
5. Reconnecte-toi au serveur → Tu es authentifié !

## 📊 Différences entre les versions

| Aspect | Spigot | BungeeCord |
|--------|--------|------------|
| **Serveur** | Un seul | Plusieurs (réseau) |
| **Installation** | Sur le serveur Minecraft | Sur le proxy BungeeCord |
| **Fichier** | MCLocalAuth.jar | MCLocalAuth-Bungee.jar |

**Ton cas** : Serveur `45.140.164.92:25606`
- Si c'est un serveur unique → **Spigot**
- Si tu as d'autres serveurs derrière un proxy → **BungeeCord**

## 🐛 Problèmes courants

### "No compiler is provided"
→ Tu as un JRE au lieu d'un JDK. Installe le JDK (étape 1)

### Le bot ne démarre pas
→ Vérifie le token dans config.yml  
→ Active SERVER MEMBERS INTENT sur Discord  

### Les commandes Discord n'apparaissent pas
→ Attends jusqu'à 1h OU définis `guild_id` pour enregistrement instantané

## 🎉 TU ES PRÊT !

Tout le code est fait et documenté.

Il te suffit de :
1. ✅ Installer le JDK
2. ✅ Compiler les plugins
3. ✅ Choisir ta version (Spigot ou BungeeCord)
4. ✅ Installer sur ton serveur
5. ✅ Configurer avec ton token Discord

**Le bot Discord démarrera automatiquement avec ton serveur Minecraft !** 🚀

---

**Version actuelle** : 1.1.0  
**Compatible** : Minecraft 1.8 à 1.21+  
**Support** : Lis la documentation en cas de problème
