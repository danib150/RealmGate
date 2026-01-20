package com.realmgate.services.events;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.realmgate.RealmGatePlugin;
import com.realmgate.services.Services;
import com.realmgate.services.types.config.RealmGateSettings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Locale;
import java.util.UUID;

public class PlayerJoinQuitEvent {

    public static void onPlayerJoin(PlayerConnectEvent event) {

        Services services = RealmGatePlugin.getInstance().getServices();
        String serverName = services.getConfigurationService().getSettings().getProperty(RealmGateSettings.DEFAULT);

        JedisPool pool = services.getRedisService().getPool();

        UUID uuid = event.getPlayerRef().getUuid();
        String name = event.getPlayerRef().getUsername();
        String nameKey = name.toLowerCase(Locale.ROOT);
        try (Jedis jedis = pool.getResource()) {
            var tx = jedis.multi();

            tx.sadd("realmgate:server:" + serverName + ":players", uuid.toString());

            tx.set("realmgate:player:" + uuid + ":server", serverName);

            tx.set("realmgate:player:" + uuid + ":name", name);

            tx.set("realmgate:player:name:" + nameKey, uuid.toString());

            tx.exec();
        }

    }

    public static void onPlayerQuit(PlayerDisconnectEvent event) {
        Services services = RealmGatePlugin.getInstance().getServices();
        String serverName = services.getConfigurationService().getSettings().getProperty(RealmGateSettings.DEFAULT);

        JedisPool pool = services.getRedisService().getPool();

        UUID uuid = event.getPlayerRef().getUuid();

        try (Jedis jedis = pool.getResource()) {

            String name = jedis.get("realmgate:player:" + uuid + ":name");

            var tx = jedis.multi();

            tx.srem("realmgate:server:" + serverName + ":players", uuid.toString());

            tx.del("realmgate:player:" + uuid + ":server");

            if (name != null) {
                tx.del("realmgate:player:name:" + name.toLowerCase(Locale.ROOT));
            }

            tx.exec();
        }

    }


}
