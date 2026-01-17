package com.realmgate.config;

import com.realmgate.server.BackendServer;

import java.util.List;
import java.util.Map;

public final class ConfigValidator {

    public void validate(String defaultServer, Map<String, BackendServer> servers, List<String> fallback) {

        if (servers.isEmpty()) {
            throw new IllegalStateException("No backend servers defined");
        }

        if (!servers.containsKey(defaultServer)) {
            throw new IllegalStateException("Default server '" + defaultServer + "' not found");
        }

        for (String fb : fallback) {
            if (!servers.containsKey(fb)) {
                throw new IllegalStateException("Fallback server '" + fb + "' not found");
            }
        }
    }
}

