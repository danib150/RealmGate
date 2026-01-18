package com.realmgate.server;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.realmgate.RealmGatePlugin;
import com.realmgate.core.RealmGateCore;
import com.realmgate.services.types.redis.RedisService;
import com.realmgate.services.types.redis.ServerHealthService;

import java.util.List;
import java.util.Map;

public class ServerRouter {

    private final Map<String, BackendServer> servers;
    private final boolean fallbackEnabled;
    private final List<String> fallbackOrder;
    private final RedisService redisService;

    public ServerRouter(Map<String, BackendServer> servers, boolean fallbackEnabled, List<String> fallbackOrder, RedisService redisService) {
        this.servers = servers;
        this.fallbackEnabled = fallbackEnabled;
        this.fallbackOrder = fallbackOrder;
        this.redisService = redisService;
    }

    public void route(PlayerSetupConnectEvent event, String requestedServer) {
        BackendServer server = selectServer(requestedServer);

        if (server == null) {
            event.setReason(fallbackEnabled ? "No backend servers are currently online" : "Requested server is unavailable");
            event.setCancelled(true);
            return;
        }

        refer(event, server);
    }

    private BackendServer selectServer(String requested) {
        BackendServer direct = servers.get(requested);
        if (isOnline(direct)) {
            return direct;
        }

        if (!fallbackEnabled) {
            return null;
        }

        for (String name : fallbackOrder) {
            BackendServer fallback = servers.get(name);
            if (isOnline(fallback)) {
                return fallback;
            }
        }

        return null;
    }


    public void transferPlayer(PlayerRef playerRef, BackendServer server) throws ServerMoveException {

        if (playerRef == null) {
            throw new ServerMoveException("Player reference is null");
        }

        if (server == null) {
            throw new ServerMoveException("Target server is null");
        }

        ServerHealthService healthService =
                RealmGatePlugin.getInstance()
                        .getServices()
                        .getServerHealthService();

        if (!healthService.isServerOnline(server.getName())) {
            throw new ServerMoveException("Server '" + server.getName() + "' is not online");
        }

        RealmGateCore core = RealmGatePlugin.getInstance().getCore();
        byte[] payload = core.getPayloadSigner().sign(playerRef.getUuid(), server);
        playerRef.referToServer(server.getAddress(), server.getPort(), payload);
    }

    private boolean isOnline(BackendServer server) {
        return server != null && redisService.isServerOnline(server.getName());
    }

    private void refer(PlayerSetupConnectEvent event, BackendServer server) {
        event.referToServer(server.getAddress(), server.getPort(), null);
    }
}
