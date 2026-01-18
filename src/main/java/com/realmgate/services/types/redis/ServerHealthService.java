package com.realmgate.services.types.redis;

import com.realmgate.services.Service;

import java.time.Duration;

public class ServerHealthService implements Service {

    private final RedisService redisService;

    public ServerHealthService(RedisService redisService, Duration heartbeatTtl) {
        this.redisService = redisService;
    }

    @Override
    public void init() {
        // nothing to schedule here (gateway does not emit heartbeats)
    }

    @Override
    public void stop() {
        // nothing to stop
    }

    public boolean isServerOnline(String serverName) {
        return redisService.exists(key(serverName));
    }

    private String key(String serverName) {
        return "realmgate:servers:" + serverName;
    }
}
