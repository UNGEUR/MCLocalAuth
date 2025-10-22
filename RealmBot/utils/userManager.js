const fs = require('fs');
const path = require('path');

class UserManager {
  constructor() {
    this.dataFile = path.join(__dirname, '..', 'users.json');
    this.users = this.loadUsers();
    this.pendingAuth = new Map(); // Map<discordId, {minecraftName, code, expiry}>
  }

  loadUsers() {
    try {
      if (fs.existsSync(this.dataFile)) {
        return JSON.parse(fs.readFileSync(this.dataFile, 'utf8'));
      }
    } catch (error) {
      console.error('Erreur lors du chargement des utilisateurs:', error);
    }
    return {};
  }

  saveUsers() {
    try {
      fs.writeFileSync(this.dataFile, JSON.stringify(this.users, null, 2));
    } catch (error) {
      console.error('Erreur lors de la sauvegarde des utilisateurs:', error);
    }
  }

  linkUser(discordId, minecraftName) {
    this.users[discordId] = {
      minecraftName,
      linkedAt: new Date().toISOString(),
      lastAuth: new Date().toISOString()
    };
    this.saveUsers();
  }

  unlinkUser(discordId) {
    if (this.users[discordId]) {
      delete this.users[discordId];
      this.saveUsers();
      return true;
    }
    return false;
  }

  getLinkedUser(discordId) {
    return this.users[discordId] || null;
  }

  isUserLinked(discordId) {
    return !!this.users[discordId];
  }

  // Gestion des authentifications en attente
  setPendingAuth(discordId, minecraftName, code, timeoutMinutes = 10) {
    const expiry = Date.now() + (timeoutMinutes * 60 * 1000);
    this.pendingAuth.set(discordId, {
      minecraftName,
      code,
      expiry
    });

    // Auto-cleanup après expiration
    setTimeout(() => {
      this.pendingAuth.delete(discordId);
    }, timeoutMinutes * 60 * 1000);
  }

  getPendingAuth(discordId) {
    const pending = this.pendingAuth.get(discordId);
    if (pending && pending.expiry > Date.now()) {
      return pending;
    }
    this.pendingAuth.delete(discordId);
    return null;
  }

  clearPendingAuth(discordId) {
    this.pendingAuth.delete(discordId);
  }

  getAllLinkedUsers() {
    return Object.keys(this.users).map(discordId => ({
      discordId,
      ...this.users[discordId]
    }));
  }

  // Nettoyage des authentifications expirées
  cleanupExpiredAuth() {
    const now = Date.now();
    for (const [discordId, auth] of this.pendingAuth.entries()) {
      if (auth.expiry <= now) {
        this.pendingAuth.delete(discordId);
      }
    }
  }
}

module.exports = UserManager;