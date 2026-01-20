package com.realmgate.server;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.realmgate.RealmGatePlugin;
import com.realmgate.core.RealmGateCore;
import com.realmgate.services.types.redis.RedisService;

import java.util.Map;

public class ServerRouter {

    private final Map<String, BackendServer> servers;
    private final RedisService redisService;

    public ServerRouter(Map<String, BackendServer> servers, RedisService redisService) {
        this.servers = servers;
        this.redisService = redisService;
    }

    private BackendServer selectServer(String requested) {
        BackendServer direct = servers.get(requested);
        if (isOnline(direct)) {
            return direct;
        }
        return null;
    }


    public void transfer(PlayerRef playerRef, String targetServer) throws ServerMoveException {

        if (playerRef == null) {
            throw new ServerMoveException("Player reference is null");
        }

        BackendServer server = selectServer(targetServer);

        if (server == null) {
            throw new ServerMoveException("Target server '" + targetServer + "' is unavailable");
        }

        refer(playerRef, server);
    }

    /*

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


     */

    private boolean isOnline(BackendServer server) {
        return server != null && redisService.isServerOnline(server.getName());
    }

    private void refer(PlayerRef playerRef, BackendServer server) {

        RealmGateCore core = RealmGatePlugin.getInstance().getCore();


        byte[] payload = core.getPayloadSigner().sign(playerRef.getUuid(), server);

        playerRef.referToServer(server.getAddress(), server.getPort(), payload);
    }
}
