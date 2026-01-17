package com.realmgate.services;

import com.realmgate.RealmGatePlugin;
import com.realmgate.services.types.EventService;
import com.realmgate.services.types.ServerHealthService;
import lombok.Getter;

import java.time.Duration;

@Getter
public class Services {

    private Service eventService, serverHealthService;

    public Services(RealmGatePlugin realmGatePlugin) {
        eventService = new EventService(realmGatePlugin.getEventRegistry());
        serverHealthService = new ServerHealthService(realmGatePlugin.getCore().getServers().values(), Duration.ofMillis(300),5);
    }

    public void load() {
        eventService.init();
        serverHealthService.init();
    }

    public void unload() {
        eventService.stop();
        serverHealthService.stop();
    }

}
