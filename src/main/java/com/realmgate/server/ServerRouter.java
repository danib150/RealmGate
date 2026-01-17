package com.realmgate.server;

import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.realmgate.RealmGatePlugin;

import java.util.List;
import java.util.Map;

public class ServerRouter {

    private final Map<String, BackendServer> servers;
    private final boolean fallbackEnabled;
    private final List<String> fallbackOrder;

    public ServerRouter(Map<String, BackendServer> servers, boolean fallbackEnabled, List<String> fallbackOrder) {
        this.servers = servers;
        this.fallbackEnabled = fallbackEnabled;
        this.fallbackOrder = fallbackOrder;
    }

    public void connectToServer(PlayerSetupConnectEvent event, BackendServer server) {
        if (server == null) {
            event.setReason("Unknown server");
            event.setCancelled(true);
            return;
        }

        if (!server.isReachable()) {
            event.setReason("Server is currently offline");
            event.setCancelled(true);
            return;
        }

        refer(event, server);
    }


    public void route(PlayerSetupConnectEvent event, String requestedServer) {
        BackendServer selected = selectServer(requestedServer);

        if (selected == null) {
            event.setReason("No backend servers available");
            event.setCancelled(true);
            return;
        }

        refer(event, selected);

        event.setReason(fallbackEnabled ? "No backend servers are currently online" : "Target server is unavailable");
    }

    private BackendServer selectServer(String requested) {
        BackendServer direct = servers.get(requested);
        if (isUsable(direct)) return direct;

        if (!fallbackEnabled) return null;

        for (String name : fallbackOrder) {
            BackendServer server = servers.get(name);
            if (isUsable(server)) {
                return server;
            }
        }

        return null;
    }

    private boolean isUsable(BackendServer server) {
        return server != null && server.isReachable();
    }


    private void refer(PlayerSetupConnectEvent event, BackendServer server) {
        RealmGatePlugin.getInstance().getLogger().atInfo().log("Redirecting %s to %s:%d", event.getUsername(), server.getAddress(), server.getPort());
        event.referToServer(server.getAddress(), server.getPort(), null);
    }
}
