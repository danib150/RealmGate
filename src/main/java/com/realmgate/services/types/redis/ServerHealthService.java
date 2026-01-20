package com.realmgate.services.types.redis;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.task.TaskRegistry;
import com.realmgate.services.Service;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ServerHealthService implements Service {

    private final RedisService redisService;
    private final TaskRegistry taskRegistry;
    private final String serverName;
    private final int heartbeatTtl;

    private ScheduledFuture<Void> heartbeatTask;

    public ServerHealthService(RedisService redisService, TaskRegistry taskRegistry, String serverName, int heartbeatTtl) {
        this.redisService = redisService;
        this.taskRegistry = taskRegistry;
        this.serverName = serverName;
        this.heartbeatTtl = heartbeatTtl;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        long period = Math.max(1, heartbeatTtl / 2);

        heartbeatTask = (ScheduledFuture<Void>) HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() ->
                redisService.publishHeartbeat(serverName), 0, period, TimeUnit.SECONDS);

        taskRegistry.registerTask(heartbeatTask);
    }

    @Override
    public void stop() {
        if (heartbeatTask != null) {
            heartbeatTask.cancel(false);
        }
    }
}
