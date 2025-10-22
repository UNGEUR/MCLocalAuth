# 🎮 Qu'est-ce que MCLocalAuth ?

## 📝 Résumé en une phrase

**MCLocalAuth est un plugin Minecraft qui sécurise ton serveur en forçant les joueurs à s'authentifier via Discord avant de pouvoir jouer.**

---

## 🎯 Problème résolu

### Sans MCLocalAuth :
❌ N'importe qui peut se connecter avec n'importe quel pseudo  
❌ Risque d'usurpation d'identité (quelqu'un prend le pseudo d'un autre)  
❌ Pas de contrôle sur qui peut accéder au serveur  
❌ Impossible de lier les joueurs Minecraft à ta communauté Discord  

### Avec MCLocalAuth :
✅ Seuls les membres de ton Discord peuvent jouer  
✅ Chaque joueur est lié à son compte Discord  
✅ Protection IP : un pseudo = une seule personne  
✅ Communauté Discord = Liste blanche automatique  

---

## 🔐 Comment ça fonctionne ?

### Pour les joueurs

**1. Première connexion**
```
Joueur → Se connecte au serveur Minecraft
         ↓
Serveur → Déconnecte immédiatement avec un message :
         
         ╔═══════════════════════════════════════╗
         ║  AUTHENTIFICATION DISCORD REQUISE     ║
         ║                                       ║
         ║  Bienvenue PlayerName !               ║
         ║  Code d'authentification : 123456     ║
         ║                                       ║
         ║  Sur Discord, tape :                  ║
         ║  /auth PlayerName 123456              ║
         ╚═══════════════════════════════════════╝
```

**2. Sur Discord**
```
Joueur → Tape /auth PlayerName 123456
         ↓
Bot    → Vérifie que le joueur est membre du serveur Discord
         ↓
Bot    → Valide le code
         ↓
Serveur → IP du joueur enregistrée
```

**3. Reconnexion**
```
Joueur → Se reconnecte au serveur
         ↓
Serveur → Reconnaît l'IP validée
         ↓
Joueur → Accès autorisé ! 🎉
```

---

## 🛡️ Système de sécurité

### Protection à 3 niveaux

**Niveau 1 : Vérification Discord**
- Le joueur DOIT être membre de ton serveur Discord
- Pas membre = Pas d'accès au serveur Minecraft
- C'est comme une liste blanche automatique

**Niveau 2 : Code temporaire**
- Chaque connexion génère un code unique (ex: 123456)
- Le code expire après 10 minutes
- Impossible de deviner ou réutiliser un ancien code

**Niveau 3 : Protection IP**
- L'IP du joueur est enregistrée lors de l'authentification
- Si quelqu'un essaie de se connecter avec le même pseudo depuis une autre IP = Bloqué
- Empêche l'usurpation d'identité

---

## 🎮 Cas d'usage

### 1. Serveur privé pour une communauté
```
Tu as un serveur Discord avec tes amis
Tu veux que seuls eux puissent jouer
→ MCLocalAuth vérifie automatiquement qui est membre
```

### 2. Serveur semi-public
```
Tu acceptes de nouveaux joueurs
Mais ils doivent d'abord rejoindre ton Discord
→ MCLocalAuth force cette étape
```

### 3. Protection anti-troll
```
Quelqu'un essaie de se connecter avec le pseudo d'un autre
→ MCLocalAuth bloque (IP différente)
```

### 4. Lien Discord-Minecraft
```
Tu sais exactement qui joue sur ton serveur
Tu peux facilement bannir/gérer via Discord
→ Une seule communauté pour tout
```

---

## ⚙️ Fonctionnalités principales

### 🤖 Bot Discord Intégré
- **Démarre automatiquement** avec le serveur Minecraft
- Pas besoin de gérer 2 applications séparées
- Commandes Discord : `/auth`, `/status`, `/unlink`

### 📱 Message de déconnexion intelligent
- Le code s'affiche dans une **popup impossible à manquer**
- Instructions claires pour le joueur
- Format professionnel et coloré

### 🔒 Système IP avancé
- Enregistrement automatique de la première IP
- Support IP multiples (pour joueurs qui changent de lieu)
- Commandes admin pour gérer les IP manuellement

### 🛠️ Commandes administrateur
```bash
/auth showips <joueur>         # Voir les IP autorisées
/auth addip <joueur> <ip>      # Ajouter une IP
/auth removeip <joueur> <ip>   # Supprimer une IP
/auth resetip <joueur>         # Réinitialiser
```

---

## 💡 Avantages

### Pour l'administrateur
1. **Sécurité maximale** - Impossible d'usurper un compte
2. **Gestion centralisée** - Tout depuis Discord
3. **Zéro configuration réseau** - Pas de ports à ouvrir
4. **Installation simple** - Un seul fichier JAR
5. **Maintenance facile** - Un seul processus à surveiller

### Pour les joueurs
1. **Processus clair** - Instructions précises
2. **Une seule authentification** - Ensuite reconnexion automatique
3. **Protection de leur compte** - Personne ne peut usurper leur identité
4. **Support multi-IP** - Peuvent jouer de différents endroits (si autorisé)

---

## 🔄 Workflow complet

```
┌─────────────────────────────────────────────────────────┐
│  1. JOUEUR SE CONNECTE                                  │
│     └─> Serveur Minecraft (45.140.164.92:25606)        │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  2. PLUGIN GÉNÈRE UN CODE                               │
│     └─> Code unique : 123456                            │
│     └─> Expire dans : 10 minutes                        │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  3. DÉCONNEXION AVEC MESSAGE                            │
│     └─> "Ton code : 123456"                             │
│     └─> "Tape /auth sur Discord"                        │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  4. JOUEUR VA SUR DISCORD                               │
│     └─> Tape : /auth PlayerName 123456                  │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  5. BOT VÉRIFIE                                         │
│     ├─> Est-il membre du Discord ? ✓                    │
│     ├─> Le code est-il valide ? ✓                       │
│     └─> Le pseudo correspond ? ✓                        │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  6. VALIDATION                                          │
│     └─> IP enregistrée dans la base de données          │
│     └─> Message de succès envoyé                        │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│  7. RECONNEXION                                         │
│     └─> Joueur se reconnecte                            │
│     └─> IP reconnue                                     │
│     └─> ACCÈS AUTORISÉ ! 🎉                             │
└─────────────────────────────────────────────────────────┘
```

---

## 🎬 Exemple concret

### Scénario : Nouveau joueur "Alex"

**Étape 1** : Alex trouve ton serveur Minecraft  
**Étape 2** : Il essaie de se connecter  
**Étape 3** : Il est déconnecté avec un message lui demandant de s'authentifier  
**Étape 4** : Il doit rejoindre ton serveur Discord  
**Étape 5** : Sur Discord, il tape `/auth Alex 123456`  
**Étape 6** : Le bot vérifie qu'il est membre  
**Étape 7** : Son IP est enregistrée  
**Étape 8** : Il se reconnecte et peut jouer  

**Le lendemain** : Alex se reconnecte → Accès direct (IP reconnue) ✅

**Un troll essaie** : Quelqu'un essaie de se connecter avec "Alex" depuis une autre IP → Bloqué ❌

---

## 🆚 Comparaison avec d'autres solutions

| Fonctionnalité | MCLocalAuth | AuthMe | Whitelist Classique |
|----------------|-------------|--------|---------------------|
| **Authentification** | Discord | Mot de passe | Liste manuelle |
| **Vérification membre** | ✅ Automatique | ❌ Non | ⚠️ Manuel |
| **Protection IP** | ✅ Oui | ⚠️ Optionnel | ❌ Non |
| **Bot intégré** | ✅ Oui | ❌ Non | ❌ Non |
| **Interface** | Discord | In-game | Fichier texte |
| **Facilité** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ |
| **Sécurité** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |

---

## 📊 Statistiques techniques

- **Taille** : 10.68 MB (inclut JDA 5.0 pour Discord)
- **Version** : 1.1.0
- **Compatible** : Minecraft 1.8 à 1.21+
- **Plateformes** : Spigot, Paper, Purpur, BungeeCord
- **Langage** : Java 8
- **Dépendances** : Aucune (tout inclus)

---

## 🎯 En résumé

**MCLocalAuth transforme ton serveur Discord en système d'authentification pour Minecraft.**

**En pratique :**
- Discord = Ton système de "comptes"
- Membre Discord = Peut jouer sur Minecraft
- Banni de Discord = Banni du serveur Minecraft
- Un seul endroit pour gérer ta communauté

**C'est comme si ton serveur Discord était la "porte d'entrée" de ton serveur Minecraft.** 🚪🔐

---

## ❓ Questions fréquentes

**Q : Et si un joueur n'a pas Discord ?**  
R : Il ne peut pas jouer. Discord est obligatoire. C'est le principe du plugin.

**Q : Et si un joueur change d'IP (déménagement, changement FAI) ?**  
R : Un admin peut ajouter sa nouvelle IP avec `/auth addip`

**Q : Le bot Discord doit être en ligne 24/7 ?**  
R : Oui, mais il démarre automatiquement avec le serveur Minecraft.

**Q : Ça marche avec un serveur crack (non-premium) ?**  
R : Oui ! C'est même particulièrement utile pour les serveurs crack.

**Q : Combien de joueurs ça supporte ?**  
R : Illimité. Testé sur des serveurs de plusieurs centaines de joueurs.

---

**MCLocalAuth v1.1 - Sécurise ton serveur, simplifie la gestion, protège tes joueurs.** 🛡️
