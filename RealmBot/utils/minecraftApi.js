const axios = require('axios');

class MinecraftApi {
  constructor(config) {
    this.host = config.host;
    this.port = config.port;
    this.token = config.token;
    this.protocol = config.protocol || 'http';
    this.baseUrl = `${this.protocol}://${this.host}:${this.port}`;
  }

  async validateCode(playerName, code, discordUserId = null) {
    try {
      const params = {
        token: this.token,
        player: playerName,
        code: code
      };
      
      // Ajouter l'ID Discord si fourni
      if (discordUserId) {
        params.discord_user_id = discordUserId;
      }
      
      const response = await axios.get(`${this.baseUrl}/mclogin`, {
        params: params,
        timeout: 10000 // 10 secondes de timeout
      });

      return {
        success: response.status === 200,
        message: response.data || 'OK'
      };
    } catch (error) {
      console.error('Erreur lors de la validation du code:', error.message);
      
      if (error.response) {
        // Le serveur a répondu avec un code d'erreur
        return {
          success: false,
          message: this.getErrorMessage(error.response.status, error.response.data)
        };
      } else if (error.request) {
        // Pas de réponse du serveur
        return {
          success: false,
          message: 'Impossible de contacter le serveur Minecraft. Vérifiez qu\'il est en ligne.'
        };
      } else {
        // Autre erreur
        return {
          success: false,
          message: `Erreur de connexion: ${error.message}`
        };
      }
    }
  }

  getErrorMessage(status, data) {
    switch (status) {
      case 400:
        if (data === 'Missing player or code') {
          return 'Paramètres manquants dans la requête.';
        } else if (data === 'Invalid code') {
          return 'Code invalide ou expiré.';
        } else if (data === 'Not pending') {
          return 'Aucune authentification en attente pour ce joueur.';
        }
        return 'Requête invalide.';
      case 401:
        return 'Token d\'authentification invalide.';
      case 403:
        return 'Vous devez être membre du serveur Discord pour vous authentifier.';
      case 404:
        return 'Joueur non connecté sur le serveur.';
      case 405:
        return 'Méthode non autorisée.';
      default:
        return `Erreur serveur (${status}): ${data || 'Erreur inconnue'}`;
    }
  }

  async testConnection() {
    try {
      // Test basique de connexion
      const response = await axios.get(`${this.baseUrl}/mclogin`, {
        params: {
          token: this.token,
          player: 'test',
          code: '0000'
        },
        timeout: 5000
      });
      return { success: true, message: 'Connexion OK' };
    } catch (error) {
      if (error.response && error.response.status === 404) {
        // 404 est attendu pour un test, cela signifie que l'API fonctionne
        return { success: true, message: 'API accessible' };
      }
      return { 
        success: false, 
        message: error.code === 'ECONNREFUSED' 
          ? 'Serveur inaccessible' 
          : error.message 
      };
    }
  }
}

module.exports = MinecraftApi;