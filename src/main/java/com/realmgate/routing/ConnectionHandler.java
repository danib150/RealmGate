package com.realmgate.routing;

import com.realmgate.server.BackendServer;

public interface ConnectionHandler {
    boolean connect(Object player, BackendServer backendServer);
}
