# 🔨 Guide de Compilation - MCLocalAuth v1.1

## 📦 Deux versions disponibles

### 1. **Version Spigot/Paper** (`plugin/`)
Pour serveurs Minecraft standalone (Spigot, Paper, Purpur...)
- Compatible Minecraft 1.8 à 1.21+
- Installation sur un seul serveur

### 2. **Version BungeeCord** (`bungeecord/`)
Pour réseaux de serveurs avec proxy BungeeCord/Waterfall
- Authentification centralisée sur le proxy
- Gestion multi-serveurs

## ⚠️ Prérequis : JDK (pas JRE !)

**Problème détecté** : Vous avez un JRE (Java Runtime Environment) au lieu d'un JDK (Java Development Kit).

Maven a besoin du **JDK** pour compiler du code Java.

## 🔧 Installer le JDK

### Option 1 : Adoptium (Recommandé)

1. Téléchargez **Eclipse Temurin JDK 8** :
   - https://adoptium.net/temurin/releases/?version=8
   - Sélectionnez : Windows x86 / JDK / .msi installer
   
2. Installez le fichier téléchargé

3. Vérifiez l'installation :
   ```powershell
   javac -version
   ```
   Vous devriez voir : `javac 1.8.0_xxx`

### Option 2 : Oracle JDK 8

1. Téléchargez sur Oracle :
   - https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
   - Téléchargez "Windows x86 JDK"
   
2. Installez et ajoutez au PATH

### Option 3 : OpenJDK Portable (Sans installation)

1. Téléchargez OpenJDK 8 :
   - https://github.com/adoptium/temurin8-binaries/releases
   - Fichier : `OpenJDK8U-jdk_x86-32_windows_hotspot_xxx.zip`
   
2. Décompressez dans `C:\jdk8`

3. Utilisez Maven avec ce JDK :
   ```powershell
   $env:JAVA_HOME="C:\jdk8"
   $env:PATH="C:\jdk8\bin;$env:PATH"
   ```

## 🚀 Compilation

### Version Spigot/Paper

```powershell
cd "C:\Users\Author\Documents\MEGA\bot et plugin\plugin"
& "C:\Users\Author\Documents\MEGA\bot et plugin\RealmBot\apache-maven-3.9.9\bin\mvn.cmd" clean package
```

Le JAR sera dans : `plugin\target\MCLocalAuth.jar`

### Version BungeeCord

```powershell
cd "C:\Users\Author\Documents\MEGA\bot et plugin\bungeecord"
& "C:\Users\Author\Documents\MEGA\bot et plugin\RealmBot\apache-maven-3.9.9\bin\mvn.cmd" clean package
```

Le JAR sera dans : `bungeecord\target\MCLocalAuth-Bungee.jar`

## 📋 Script de compilation automatique

J'ai créé `COMPILER.bat` mais il nécessite Maven dans le PATH.

**Version modifiée pour votre environnement** :

```batch
@echo off
echo Compilation MCLocalAuth v1.1
echo.

set MAVEN_PATH=C:\Users\Author\Documents\MEGA\bot et plugin\RealmBot\apache-maven-3.9.9\bin\mvn.cmd

echo [1/2] Compilation version Spigot...
cd plugin
call "%MAVEN_PATH%" clean package
if %errorlevel% equ 0 (
    echo ✅ Version Spigot compilée : plugin\target\MCLocalAuth.jar
) else (
    echo ❌ Erreur compilation Spigot
)

echo.
echo [2/2] Compilation version BungeeCord...
cd ..\bungeecord
call "%MAVEN_PATH%" clean package
if %errorlevel% equ 0 (
    echo ✅ Version BungeeCord compilée : bungeecord\target\MCLocalAuth-Bungee.jar
) else (
    echo ❌ Erreur compilation BungeeCord
)

cd ..
pause
```

## 📊 Différences entre les versions

| Fonctionnalité | Spigot/Paper | BungeeCord |
|----------------|--------------|------------|
| **Installation** | Sur chaque serveur | Sur le proxy uniquement |
| **Authentification** | Par serveur | Centralisée (tout le réseau) |
| **Bot Discord** | Intégré au serveur | Intégré au proxy |
| **Use case** | Serveur unique | Réseau multi-serveurs |
| **Fichier** | MCLocalAuth.jar | MCLocalAuth-Bungee.jar |
| **Dossier** | `plugins/` | `plugins/` (du proxy) |

## 🎯 Quelle version choisir ?

### Utilisez **Spigot/Paper** si :
- ✅ Vous avez un seul serveur Minecraft
- ✅ Pas de proxy BungeeCord/Waterfall
- ✅ Serveur standalone

### Utilisez **BungeeCord** si :
- ✅ Vous avez plusieurs serveurs (Lobby, Survival, Creative...)
- ✅ Vous utilisez BungeeCord ou Waterfall
- ✅ Vous voulez une authentification unique pour tout le réseau

## ⚡ Compilation rapide (après installation JDK)

Une fois le JDK installé :

```powershell
# Compiler les DEUX versions
cd "C:\Users\Author\Documents\MEGA\bot et plugin"

# Version Spigot
cd plugin
mvn clean package

# Version BungeeCord
cd ..\bungeecord
mvn clean package
```

## 🐛 Dépannage

### Erreur "No compiler is provided"
```
❌ Vous avez un JRE au lieu d'un JDK
✅ Installez un JDK (voir section ci-dessus)
```

### Erreur "mvn not found"
```
❌ Maven n'est pas dans le PATH
✅ Utilisez le chemin complet vers mvn.cmd
✅ Ou ajoutez Maven au PATH système
```

### Tests échouent
```
✅ Utilisez : mvn clean package -DskipTests
```

## 📝 Note sur les versions

- **Version actuelle** : 1.1.0
- **Minecraft supporté** : 1.8 à 1.21+
- **Java requis** : Java 8+ (compilé en Java 8 pour compatibilité)
- **Bot Discord** : JDA 5.0 (intégré dans les deux versions)

## 🎉 Après compilation

1. **Version Spigot** : 
   - Copiez `plugin/target/MCLocalAuth.jar` dans `plugins/` de votre serveur
   
2. **Version BungeeCord** :
   - Copiez `bungeecord/target/MCLocalAuth-Bungee.jar` dans `plugins/` du proxy BungeeCord

3. Configurez `config.yml` avec votre token Discord

4. Redémarrez le serveur/proxy

Le bot Discord démarrera automatiquement ! 🚀
