# ⚠️ Version BungeeCord - En développement

## 📝 Statut

La version BungeeCord de MCLocalAuth est **partiellement implémentée**.

### ✅ Ce qui existe
- Structure Maven complète (`pom.xml`)
- Configuration (`bungee.yml`, `config.yml`)
- Classe principale (`MCLocalAuthBungee.java`)

### ❌ Ce qui manque
- Classes de gestion (`PendingManager`, `Storage`, etc.)
- Listener de connexion (`LoginListener`)
- Intégration Discord (`DiscordBot`, `DiscordCommandListener`)
- Classes utilitaires

## 🎯 Pour la version Spigot/Paper

La **version Spigot/Paper est complète et fonctionnelle** :
- ✅ Compilée : `Plugins/MCLocalAuth-Spigot-v1.1.0.jar`
- ✅ Testée et prête à l'emploi
- ✅ Bot Discord intégré
- ✅ Toutes les fonctionnalités implémentées

## 🔧 Pour compléter la version BungeeCord

Il faudrait adapter les classes suivantes de la version Spigot :

1. **auth/PendingManager.java** - Gestion des codes temporaires
2. **storage/Storage.java** - Stockage IP et données
3. **listeners/LoginListener.java** - Gestion connexions (API BungeeCord)
4. **discord/DiscordBot.java** - Bot Discord intégré
5. **discord/DiscordCommandListener.java** - Commandes Discord
6. **util/CodeUtil.java** - Génération codes
7. **util/DiscordValidator.java** - Validation Discord

### Différences API Spigot vs BungeeCord

**Spigot** :
```java
org.bukkit.Bukkit
org.bukkit.entity.Player
org.bukkit.event.player.PlayerJoinEvent
```

**BungeeCord** :
```java
net.md_5.bungee.api.ProxyServer
net.md_5.bungee.api.connection.ProxiedPlayer
net.md_5.bungee.api.event.PostLoginEvent
```

## 💡 Recommandation

Pour un serveur unique, utilisez la **version Spigot/Paper** qui est complète et fonctionnelle.

La version BungeeCord sera utile seulement si vous avez :
- Plusieurs serveurs Minecraft (Lobby, Survival, Creative, etc.)
- Un proxy BungeeCord/Waterfall
- Besoin d'une authentification centralisée

## 📌 Alternative

Si vous avez besoin d'authentification sur un réseau BungeeCord, vous pouvez :
1. Installer la version Spigot sur chaque serveur backend
2. Partager la même base de données entre serveurs
3. Configurer le même bot Discord pour tous

Cela fonctionnera correctement même sans version BungeeCord native.

---

**Version actuelle disponible** : MCLocalAuth-Spigot v1.1.0 ✅  
**Version BungeeCord** : En développement ⏳
