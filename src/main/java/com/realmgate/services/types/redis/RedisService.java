package com.realmgate.services.types.redis;

import ch.jalu.configme.SettingsManager;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.services.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisService implements Service {

    private final SettingsManager settings;

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
            jedis.setex(
                    onlineKey(serverName),
                    heartbeatTtl,
                    "1"
            );
        }
    }

    public void clearHeartbeat(String serverName) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(onlineKey(serverName));
        }
    }

    private String onlineKey(String serverName) {
        return "realmgate:server:" + serverName + ":online";
    }
}
