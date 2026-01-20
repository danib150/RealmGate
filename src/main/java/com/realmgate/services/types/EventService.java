package com.realmgate.services.types;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.realmgate.services.Service;
import com.realmgate.services.events.PlayerJoinQuitEvent;
import com.realmgate.services.events.PlayerSetupEvent;

public class EventService implements Service {

    private final EventRegistry e;

    public EventService(EventRegistry e) {
        this.e = e;
    }

    @Override
    public void init() {
        e.register(PlayerSetupConnectEvent.class, PlayerSetupEvent::onPlayerConnect);
        e.register(PlayerConnectEvent.class, PlayerJoinQuitEvent::onPlayerJoin);
        e.register(PlayerDisconnectEvent.class, PlayerJoinQuitEvent::onPlayerQuit);
    }

    @Override
    public void stop() {

    }
}
