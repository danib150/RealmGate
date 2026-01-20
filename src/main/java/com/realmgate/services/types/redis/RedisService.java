package com.realmgate.services.types.redis;

import ch.jalu.configme.SettingsManager;
import com.realmgate.server.ServerMoveException;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.services.Service;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RedisService implements Service {

    private final SettingsManager settings;

    @Getter
    private JedisPool pool;
    private int heartbeatTtl;

    public RedisService(SettingsManager settings) {
        this.settings = settings;
    }


    @Override
    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(8);
        config.setMaxIdle(8);
        config.setMinIdle(1);

        String host = settings.getProperty(RealmGateSettings.REDIS_HOST);
        int port = settings.getProperty(RealmGateSettings.REDIS_PORT);
        int database = settings.getProperty(RealmGateSettings.REDIS_DATABASE);
        int timeout = settings.getProperty(RealmGateSettings.REDIS_TIMEOUT_MS);
        String password = settings.getProperty(RealmGateSettings.REDIS_PASSWORD);

        this.heartbeatTtl = settings.getProperty(RealmGateSettings.REDIS_HEARTBEAT_TTL);

        if (!password.isEmpty()) {
            pool = new JedisPool(config, host, port, timeout, password, database);
        } else {
            pool = new JedisPool(config, host, port, timeout, null, database);
        }
    }

    @Override
    public void stop() {
        if (pool != null) {
            pool.close();
        }
    }

    public boolean isServerOnline(String serverName) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(onlineKey(serverName));
        }
    }

    public void publishHeartbeat(String serverName) {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(onlineKey(serverName), heartbeatTtl, "1");
        }
    }

    public Set<UUID> getPlayersWithPendingMove() {
        try (Jedis jedis = pool.getResource()) {

            return jedis.smembers("realmgate:moving").stream().map(UUID::fromString).collect(Collectors.toSet());
        }
    }


    /**
     * Consume a pending move request for a player.
     *
     * @return target server name or null if none
     */
    public String pollMoveRequest(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {

            String key = "realmgate:player:" + uuid + ":move";
            String target = jedis.get(key);

            if (target != null) {
                jedis.del(key);
                jedis.srem("realmgate:moving", uuid.toString());
            }

            return target;
        }
    }


    public void sendMoveRequest(String username, String targetServer) throws ServerMoveException {
        UUID uuid = getPlayerUuidByName(username);
        if (uuid == null) {
            throw new ServerMoveException("Cannot send player to " + targetServer + " " + username + " not found!");
        }

        sendMoveRequest(uuid, targetServer);
    }

    public void sendMoveRequest(UUID uuid, String targetServer) {
        try (Jedis jedis = pool.getResource()) {

            String moveKey = "realmgate:player:" + uuid + ":move";

            if (jedis.exists(moveKey)) {
                return;
            }

            jedis.setex(moveKey, 10, targetServer);
            jedis.sadd("realmgate:moving", uuid.toString());
        }
    }


    public UUID getPlayerUuidByName(String username) {
        try (Jedis jedis = pool.getResource()) {

            String uuid = jedis.get("realmgate:player:name:" + username.toLowerCase(Locale.ROOT));

            return uuid != null ? UUID.fromString(uuid) : null;
        }
    }

    public String getPlayerServer(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("realmgate:player:" + uuid + ":server");
        }
    }

    public void clearHeartbeat(String serverName) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(onlineKey(serverName));
        }
    }

    public boolean exists(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    private String onlineKey(String serverName) {
        return "realmgate:server:" + serverName + ":online";
    }
}
