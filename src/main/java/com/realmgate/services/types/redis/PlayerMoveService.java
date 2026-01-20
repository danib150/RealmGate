package com.realmgate.services.types.redis;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.task.TaskRegistry;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.realmgate.RealmGatePlugin;
import com.realmgate.server.ServerRouter;
import com.realmgate.services.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PlayerMoveService implements Service {

    private final RedisService redisService;
    private final TaskRegistry taskRegistry;
    private final ServerRouter serverRouter;

    private ScheduledFuture<Void> moveTask;

    public PlayerMoveService(RedisService redisService, TaskRegistry taskRegistry, ServerRouter serverRouter) {
        this.redisService = redisService;
        this.taskRegistry = taskRegistry;
        this.serverRouter = serverRouter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        moveTask = (ScheduledFuture<Void>) HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(this::tick, 250, 250, TimeUnit.MILLISECONDS);
        taskRegistry.registerTask(moveTask);
    }

    private void tick() {
        Set<UUID> moving = redisService.getPlayersWithPendingMove();
        if (moving.isEmpty()) return;

        for (UUID uuid : moving) {

            PlayerRef ref = Universe.get().getPlayer(uuid);

            if (ref == null) {
                continue;
            }

            String targetServer = redisService.pollMoveRequest(uuid);
            if (targetServer == null) continue;

            try {
                serverRouter.transfer(ref, targetServer);
            } catch (Exception e) {
                RealmGatePlugin.getInstance()
                        .getLogger()
                        .atSevere()
                        .log("Failed to move player " + uuid + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        if (moveTask != null) {
            moveTask.cancel(false);
        }
    }
}
