package com.realmgate.routing;

import com.realmgate.server.BackendServer;

import java.util.List;
import java.util.Map;

public class Router {

    private final Map<String, BackendServer> servers;
    private final List<String> fallbackOrder;
    private final boolean fallbackEnabled;

    public Router(Map<String, BackendServer> servers, boolean fallbackEnabled, List<String> fallbackOrder) {
        this.servers = servers;
        this.fallbackEnabled = fallbackEnabled;
        this.fallbackOrder = fallbackOrder;
    }

    /**
     * Routes a player to the given target server.
     *
     * @return true if routing succeeded
     */
    public boolean route(Object player, String targetServer, ConnectionHandler handler) {

        BackendServer target = servers.get(targetServer);

        if (target != null) {
            if (handler.connect(player, target)) {
                return true;
            }
        }

        if (!fallbackEnabled) {
            return false;
        }

        for (String fallback : fallbackOrder) {
            BackendServer server = servers.get(fallback);
            if (server == null) {
                continue;
            }

            if (handler.connect(player, server)) {
                return true;
            }
        }

        return false;
    }
}
