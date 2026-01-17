package com.realmgate.server;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BackendServer {

    private final String name;
    private final String address;
    private final int port;
    @Getter
    @Setter
    private boolean reachable;

    public BackendServer(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        reachable = false;
    }


}

