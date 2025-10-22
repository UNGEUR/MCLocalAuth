package fr.mclocalauth.mclocalauth.auth;

import fr.mclocalauth.mclocalauth.MCLocalAuthBungee;
import fr.mclocalauth.mclocalauth.util.CodeUtil;
// import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class PendingManager {
    public static class PendingEntry {
        public final UUID uuid;
        public final String code;
        public final long createdAt;
        public final String ip;
        public PendingEntry(UUID uuid, String code, long createdAt, String ip) {
            this.uuid = uuid; this.code = code; this.createdAt = createdAt; this.ip = ip;
        }
    }

    private final MCLocalAuthBungee plugin;
    private final Map<UUID, PendingEntry> pending = new HashMap<UUID, PendingEntry>();
    private final int codeLen;
    private final int expireSeconds;

    public PendingManager(MCLocalAuthBungee plugin, int codeLen, int expireSeconds) {
        this.plugin = plugin;
        this.codeLen = codeLen;
        this.expireSeconds = expireSeconds;
    }

    public PendingEntry createFor(ProxiedPlayer p) {
        String code = CodeUtil.numericCode(codeLen);
        String ip = p.getAddress() != null ? p.getAddress().getAddress().getHostAddress() : "";
        PendingEntry entry = new PendingEntry(p.getUniqueId(), code, System.currentTimeMillis(), ip);
        pending.put(p.getUniqueId(), entry);
        return entry;
    }

    public boolean isPending(UUID uuid) {
        cleanupExpired();
        return pending.containsKey(uuid);
    }

    public PendingEntry get(UUID uuid) {
        cleanupExpired();
        return pending.get(uuid);
    }

    public boolean validate(UUID uuid, String code) {
        return validate(uuid, code, true);
    }
    
    public boolean validate(UUID uuid, String code, boolean consume) {
        cleanupExpired();
        PendingEntry e = pending.get(uuid);
        if (e == null) return false;
        if (!e.code.equals(code)) return false;
        if (consume) {
            pending.remove(uuid);
        }
        return true;
    }

    private void cleanupExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, PendingEntry>> it = pending.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, PendingEntry> en = it.next();
            if ((now - en.getValue().createdAt) / 1000L > expireSeconds) {
                it.remove();
            }
        }
    }
}
