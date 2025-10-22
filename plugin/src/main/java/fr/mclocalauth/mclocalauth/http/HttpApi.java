package fr.mclocalauth.mclocalauth.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.mclocalauth.mclocalauth.MCLocalAuthPlugin;
import fr.mclocalauth.mclocalauth.auth.PendingManager;
import fr.mclocalauth.mclocalauth.storage.Storage;
import fr.mclocalauth.mclocalauth.util.DiscordValidator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

public class HttpApi {
    private final MCLocalAuthPlugin plugin;
    private final PendingManager pending;
    private final Storage storage;
    private final String host;
    private final int port;
    private final String token;
    private final DiscordValidator discordValidator;
    private HttpServer server;

    public HttpApi(MCLocalAuthPlugin plugin, PendingManager pending, Storage storage, String host, int port, String token) {
        this.plugin = plugin; this.pending = pending; this.storage = storage; this.host = host; this.port = port; this.token = token;
        this.discordValidator = new DiscordValidator(plugin);
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/validate", new ValidateHandler());
        server.createContext("/mclogin", new LoginHandler()); // CompatibilitÃ© ancienne version
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    public void stop() {
        if (server != null) server.stop(0);
    }
    
    // Helper pour lire tous les bytes d'un InputStream (Java 8 compatible)
    private static byte[] _readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    class ValidateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, false, "Method Not Allowed");
                return;
            }
            
            // VÃ©rifier l'authentification
            String auth = exchange.getRequestHeaders().getFirst("Authorization");
            if (auth == null || !auth.equals("Bearer " + token)) {
                sendJson(exchange, 401, false, "Unauthorized");
                return;
            }
            
            // Lire le body JSON
            String body = new String(_readAllBytes(exchange.getRequestBody()), StandardCharsets.UTF_8);
            try {
                org.json.JSONObject json = new org.json.JSONObject(body);
                String discordId = json.optString("discordId", null);
                String discordUsername = json.optString("discordUsername", null);
                String code = json.optString("code", null);
                
                if (code == null || code.isEmpty()) {
                    sendJson(exchange, 400, false, "Missing code parameter");
                    return;
                }
                
                // Trouver le joueur avec ce code en attente
                Player targetPlayer = null;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (pending.isPending(p.getUniqueId())) {
                        if (pending.validate(p.getUniqueId(), code, false)) { // VÃ©rification sans consommer
                            targetPlayer = p;
                            break;
                        }
                    }
                }
                
                if (targetPlayer == null) {
                    sendJson(exchange, 400, false, "Invalid code or no player waiting for this code");
                    return;
                }
                
                // Traitement dans le thread principal
                Player finalPlayer = targetPlayer;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        // Valider et consommer le code
                        boolean valid = pending.validate(finalPlayer.getUniqueId(), code);
                        if (valid) {
                            String ip = finalPlayer.getAddress() != null ? finalPlayer.getAddress().getAddress().getHostAddress() : "";
                            storage.setIp(finalPlayer.getUniqueId(), ip, finalPlayer.getName());
                            String msg = plugin.getConfig().getString("messages.validated", "Authentification rÃ©ussie");
                            finalPlayer.sendMessage(plugin.getConfig().getString("messages.prefix", "[MCLocalAuth] ") + msg);
                            
                            // Journalisation
                            plugin.getLogger().info("Authentification rÃ©ussie pour " + finalPlayer.getName() + 
                                                   " (Discord: " + discordUsername + "#" + discordId + ")");
                            
                            sendJson(exchange, 200, true, "Authentication successful");
                        } else {
                            sendJson(exchange, 400, false, "Code validation failed");
                        }
                    } catch (IOException e) {
                        plugin.getLogger().warning("Erreur lors de l'envoi de la rÃ©ponse HTTP: " + e.getMessage());
                    }
                });
                
            } catch (Exception e) {
                plugin.getLogger().warning("Erreur lors du parsing JSON: " + e.getMessage());
                sendJson(exchange, 400, false, "Invalid JSON format");
            }
        }
        
        private void sendJson(HttpExchange exchange, int statusCode, boolean success, String message) throws IOException {
            org.json.JSONObject response = new org.json.JSONObject();
            response.put("success", success);
            response.put("message", message);
            response.put("timestamp", System.currentTimeMillis());
            
            byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
    
    class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "Method Not Allowed");
                return;
            }
            Map<String, String> q = parseQuery(exchange.getRequestURI());
            String providedToken = q.get("token");
            if (providedToken == null || !providedToken.equals(token)) {
                send(exchange, 401, "Unauthorized");
                return;
            }
            String playerName = q.get("player");
            String code = q.get("code");
            String discordUserId = q.get("discord_user_id");
            if (playerName == null || code == null) {
                send(exchange, 400, "Missing player or code");
                return;
            }
            
            // VÃ©rification Discord si activÃ©e
            if (discordValidator.isDiscordEnabled()) {
                if (discordUserId == null || discordUserId.isEmpty()) {
                    send(exchange, 400, "Discord user ID required");
                    return;
                }
                
                if (!discordValidator.isUserInGuild(discordUserId)) {
                    send(exchange, 403, "User not in Discord server");
                    return;
                }
            }
            Player p = Bukkit.getPlayerExact(playerName);
            if (p == null) {
                send(exchange, 404, "Player not online");
                return;
            }
            // Handle validation synchronously
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    if (!pending.isPending(p.getUniqueId())) {
                        send(exchange, 400, "Not pending");
                        return;
                    }
                    boolean valid = pending.validate(p.getUniqueId(), code);
                    if (valid) {
                        String ip = p.getAddress() != null ? p.getAddress().getAddress().getHostAddress() : "";
                        storage.setIp(p.getUniqueId(), ip, p.getName());
                        String msg = plugin.getConfig().getString("messages.validated", "Authentification OK");
                        p.sendMessage(plugin.getConfig().getString("messages.prefix", "[Auth] ") + msg);
                        send(exchange, 200, "OK");
                    } else {
                        send(exchange, 400, "Invalid code");
                    }
                } catch (IOException e) {
                    // ignore
                }
            });
        }

        private Map<String, String> parseQuery(URI uri) {
            Map<String, String> map = new HashMap<String, String>();
            String q = uri.getRawQuery();
            if (q == null) return map;
            for (String pair : q.split("&")) {
                int idx = pair.indexOf('=');
                if (idx > 0) {
                    try {
                        String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                        String val = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                        map.put(key, val);
                    } catch (UnsupportedEncodingException e) {
                        // UTF-8 should always be supported
                    }
                }
            }
            return map;
        }

        private void send(HttpExchange ex, int code, String body) throws IOException {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            ex.sendResponseHeaders(code, bytes.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
