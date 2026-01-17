package com.realmgate.services.events;

import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.realmgate.RealmGatePlugin;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.core.RealmGateCore;


public class PlayerConnectEvent {

    public static void onPlayerConnect(PlayerSetupConnectEvent event) {
        RealmGateCore core = RealmGatePlugin.getInstance().getCore();
        core.getServerRouter().route(event, core.getConfiguration().getProperty(RealmGateSettings.DEFAULT_SERVER));
        RealmGatePlugin.getInstance().getLogger().atInfo().log("Successfully connected to server");
    }

}
