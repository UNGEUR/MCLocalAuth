# 🔐 Guide - Système IP Multiples MCLocalAuth

## 📋 Vue d'ensemble

Le plugin MCLocalAuth v2.0 inclut maintenant un système avancé de gestion des IP multiples qui permet :

- **Enregistrement automatique** de la première IP de connexion
- **Blocage automatique** des tentatives de connexion depuis d'autres IP
- **Gestion admin** pour ajouter/supprimer des IP autorisées
- **Protection anti-usurpation** d'identité

## 🎯 Fonctionnement

### Pour les joueurs :

1. **Première connexion** : L'IP est automatiquement enregistrée après authentification Discord
2. **Connexions suivantes** : Seules les IP autorisées peuvent se connecter
3. **Changement d'IP** : Connexion bloquée, doit contacter un admin

### Pour les administrateurs :

**Commandes disponibles :**
- `/auth showips <joueur>` - Voir toutes les IP autorisées
- `/auth addip <joueur> <ip>` - Ajouter une IP autorisée
- `/auth removeip <joueur> <ip>` - Supprimer une IP
- `/auth resetip <joueur>` - Réinitialiser toutes les IP
- `/auth setip <joueur> <ip>` - Définir IP principale

## 💻 Exemples d'utilisation

### Scénario 1 : Joueur qui déménage
```
Joueur: "Je ne peux plus me connecter depuis chez moi"
Admin: /auth addip PvP_UNGEUR 192.168.1.100
```

### Scénario 2 : Joueur avec PC + téléphone
```
Admin: /auth showips PvP_UNGEUR
       > 1. 192.168.1.50 (principale)
Admin: /auth addip PvP_UNGEUR 10.0.0.25
```

### Scénario 3 : IP compromise
```
Admin: /auth removeip PvP_UNGEUR 192.168.1.50
Admin: /auth showips PvP_UNGEUR
       > 1. 10.0.0.25 (principale)
```

## 🔒 Sécurité

### Protection automatique :
- ✅ **Blocage usurpation** : Impossible de jouer avec le pseudo d'un autre
- ✅ **Logs de sécurité** : Toutes les tentatives sont enregistrées
- ✅ **Message détaillé** : Le joueur voit son IP actuelle
- ✅ **Validation IP** : Format IPv4 vérifié

### Messages d'erreur pour les joueurs :
```
§cConnexion refusée: Votre IP n'est pas autorisée pour ce compte.

§7Votre IP: §f192.168.1.200

§eContactez un administrateur si vous jouez depuis un nouvel endroit.
```

## 📂 Structure des données

Le fichier `data.yml` stocke maintenant :
```yaml
users:
  uuid-du-joueur:
    ips:
      - "192.168.1.50"     # IP principale
      - "10.0.0.25"        # IP secondaire
    name: "PvP_UNGEUR"
    lastIp: "192.168.1.50"
    lastLogin: 1234567890
```

## 🚨 Cas d'usage typiques

### 1. **Joueur normal** (1 IP)
- Se connecte toujours du même endroit
- Aucune intervention admin nécessaire

### 2. **Joueur mobile** (2+ IP)
- Joue de chez lui + chez des amis
- Admin ajoute les IP avec `/auth addip`

### 3. **Joueur suspect** (tentative usurpation)
- Quelqu'un essaie de voler le compte
- Connexion automatiquement bloquée
- Admin alerté dans les logs

### 4. **Migration/Déménagement**
- Joueur change de connexion internet
- Admin ajoute nouvelle IP et supprime l'ancienne

## 🛠️ Administration

### Bonnes pratiques :
- **Vérifier l'identité** avant d'ajouter une IP
- **Nettoyer régulièrement** les anciennes IP
- **Surveiller les logs** pour détecter les tentatives
- **Utiliser Discord** pour valider les demandes

### Permissions requises :
- `mclocalauth.admin` - Toutes les commandes IP

## 🔧 Configuration

Nouveau message dans `config.yml` :
```yaml
messages:
  ipNotAuthorized: "§cConnexion refusée: Votre IP n'est pas autorisée pour ce compte.\n\n§7Votre IP: §f%current_ip%\n\n§eContactez un administrateur si vous jouez depuis un nouvel endroit."
```

## ✅ Avantages du système

1. **Sécurité renforcée** : Impossible d'usurper l'identité d'un joueur
2. **Flexibilité** : Support de plusieurs IP par joueur
3. **Transparence** : Joueur voit son IP actuelle en cas d'erreur
4. **Logs complets** : Traçabilité de toutes les actions
5. **Interface simple** : Commandes intuitives pour les admins

---

🎮 **Le système est maintenant actif !** Les nouveaux joueurs auront leur IP enregistrée automatiquement après authentification Discord.