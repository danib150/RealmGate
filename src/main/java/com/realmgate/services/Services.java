package com.realmgate.services;

import ch.jalu.configme.SettingsManager;
import com.hypixel.hytale.server.core.io.ServerManager;
import com.hypixel.hytale.server.core.task.TaskRegistry;
import com.realmgate.RealmGatePlugin;
import com.realmgate.server.ServerRouter;
import com.realmgate.services.types.EventService;
import com.realmgate.services.types.config.ConfigurationService;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.services.types.mysql.MySQLService;
import com.realmgate.services.types.redis.PlayerMoveService;
import com.realmgate.services.types.redis.RedisService;
import com.realmgate.services.types.redis.ServerHealthService;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.time.Duration;

@Getter
public class Services {

    private EventService eventService;
    private ConfigurationService configurationService;
    private ServerHealthService serverHealthService;
    private RedisService redisService;
    private MySQLService mysqlService;
    private SettingsManager settingsManager;
    private PlayerMoveService playerMoveService;

    public void load() {
        configurationService = new ConfigurationService();
        settingsManager = configurationService.getSettings();
        configurationService.init();

        eventService = new EventService(RealmGatePlugin.getInstance().getEventRegistry());
        eventService.init();

        mysqlService = new MySQLService(settingsManager);
        mysqlService.init();

        redisService = new RedisService(settingsManager);
        redisService.init();
    }

    public void load(TaskRegistry taskRegistry, ServerRouter router) {
        if (settingsManager == null || redisService == null) {
            throw new IllegalStateException("Services.load() must be called before load(TaskRegistry)");
        }
        String serverName = settingsManager.getProperty(RealmGateSettings.DEFAULT);
        int ttl = settingsManager.getProperty(RealmGateSettings.REDIS_HEARTBEAT_TTL);

        serverHealthService = new ServerHealthService(redisService, taskRegistry, serverName, ttl);
        serverHealthService.init();

        playerMoveService = new PlayerMoveService(redisService, taskRegistry, router);
        playerMoveService.init();
    }

    public void unload() {
        if (serverHealthService != null) serverHealthService.stop();
        if (redisService != null) redisService.stop();
        if (mysqlService != null) mysqlService.stop();
        if (eventService != null) eventService.stop();
        if (configurationService != null) configurationService.stop();
    }

}
