package com.realmgate.services.types.redis;

import com.hypixel.hytale.server.core.HytaleServer;
import com.realmgate.model.RealmGatePlayer;
import com.realmgate.server.BackendServer;
import com.realmgate.services.Service;
import com.realmgate.services.types.mysql.ServerRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

public class PlayerPresenceService implements Service {

    private final JedisPool pool;
    private final ServerRepository serverRepository;

    public PlayerPresenceService(JedisPool pool, ServerRepository serverRepository) {
        this.pool = pool;
        this.serverRepository = serverRepository;
    }

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

    public RealmGatePlayer getPlayer(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {

            String server = jedis.get("realmgate:player:" + uuid + ":server");

            if (server == null) {
                return null;
            }

            String name = jedis.get("realmgate:player:" + uuid + ":name");

            if (name == null) {
                return null;
            }

            return new RealmGatePlayer(uuid, name, server);
        }
    }

    public RealmGatePlayer getPlayer(String username) {
        try (Jedis jedis = pool.getResource()) {

            String uuidStr = jedis.get("realmgate:player:name:" + username.toLowerCase(Locale.ROOT));

            if (uuidStr == null) {
                return null;
            }

            UUID uuid = UUID.fromString(uuidStr);

            return getPlayer(uuid);
        }
    }

    /**
     * Get all Online Players of Network
     */

    public List<RealmGatePlayer> getAllOnlinePlayers() {
        try (Jedis jedis = pool.getResource()) {

            List<RealmGatePlayer> players = new ArrayList<>();

            for (BackendServer server : serverRepository.findAll()) {

                String serverName = server.getName();

                Set<String> uuids = jedis.smembers("realmgate:server:" + serverName + ":players");

                for (String uuidStr : uuids) {

                    UUID uuid = UUID.fromString(uuidStr);

                    String actualServer = jedis.get("realmgate:player:" + uuid + ":server");

                    //TODO checks
                    if (!serverName.equals(actualServer)) {
                        continue;
                    }

                    String name = jedis.get("realmgate:player:" + uuid + ":name");

                    if (name == null) {
                        continue;
                    }

                    players.add(new RealmGatePlayer(uuid, name, serverName));
                }
            }

            return players;
        }

    }
}
