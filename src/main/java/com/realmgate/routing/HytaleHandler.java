package com.realmgate.routing;

import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.realmgate.RealmGatePlugin;
import com.realmgate.server.BackendServer;

public class HytaleHandler implements ConnectionHandler {

    @Override
    public boolean connect(Object context, BackendServer backendServer) {
        if (!(context instanceof PlayerSetupConnectEvent event)) {
            return false;
        }

        try {
            RealmGatePlugin.getInstance().getLogger().atInfo().log("Redirecting player to %s:%d", backendServer.getAddress(), backendServer.getPort());
            event.referToServer(backendServer.getAddress(), backendServer.getPort());
            return true;
        } catch (Exception e) {
            RealmGatePlugin.getInstance().getLogger().atInfo().log("Failed to redirect a player to %s:%d", backendServer.getAddress(), backendServer.getPort());
            return false;
        }
    }
}
