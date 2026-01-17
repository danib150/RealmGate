package com.realmgate.services.types;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.realmgate.services.Service;
import com.realmgate.services.events.PlayerConnectEvent;

public class EventService implements Service {

    private final EventRegistry e;

    public EventService(EventRegistry e) {
        this.e = e;
    }

    @Override
    public void init() {
        e.register(PlayerSetupConnectEvent.class, PlayerConnectEvent::onPlayerConnect);
    }

    @Override
    public void stop() {

    }
}
