package com.realmgate.model;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

public final class RealmGatePlayer {

    @Getter
    private final UUID uuid;
    @Getter private String username, serverName;

    public RealmGatePlayer(UUID uuid, String username, String serverName) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.username = Objects.requireNonNull(username, "username");
        this.serverName = serverName;
    }

    public boolean isOnline() {
        return serverName != null;
    }


    @Override
    public String toString() {
        return "RealmGatePlayer{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}
