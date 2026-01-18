package com.realmgate.services;

import ch.jalu.configme.SettingsManager;
import com.realmgate.RealmGatePlugin;
import com.realmgate.services.types.EventService;
import com.realmgate.services.types.config.ConfigurationService;
import com.realmgate.services.types.mysql.MySQLService;
import com.realmgate.services.types.redis.RedisService;
import com.realmgate.services.types.redis.ServerHealthService;
import lombok.Getter;

import java.time.Duration;

@Getter
public class Services {

    private EventService eventService;
    private ConfigurationService configurationService;
    private ServerHealthService serverHealthService;
    private RedisService redisService;
    private MySQLService mysqlService;

    public void load() {
        configurationService = new ConfigurationService();
        SettingsManager settingsManager = configurationService.getSettings();
        configurationService.init();

        eventService = new EventService(RealmGatePlugin.getInstance().getEventRegistry());
        eventService.init();

        mysqlService = new MySQLService(settingsManager);
        mysqlService.init();

        redisService = new RedisService(settingsManager);
        redisService.init();

        serverHealthService = new ServerHealthService(redisService, Duration.ofMillis(300));
        serverHealthService.init();
    }

    public void unload() {
        configurationService.stop();
        eventService.stop();
        serverHealthService.stop();
        mysqlService.stop();
        redisService.stop();
    }

}
