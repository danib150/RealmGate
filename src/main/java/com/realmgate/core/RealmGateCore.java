package com.realmgate.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.config.ServerConfigLoader;
import com.realmgate.routing.Router;
import com.realmgate.server.BackendServer;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RealmGateCore {

    private final SettingsManager configuration;
    private final ServerConfigLoader serverConfigLoader;
    @Getter private Router router;

    private final Map<String, BackendServer> servers = new HashMap<>();

    public RealmGateCore(SettingsManager configuration) {
        this.configuration = configuration;
        this.serverConfigLoader = new ServerConfigLoader(configuration);
        initRouter();
    }



    public void reload() {
        configuration.reload();

        Map<String, BackendServer> loaded = serverConfigLoader.getBackendServers();

        servers.clear();
        servers.putAll(loaded);
        initRouter();
    }

    private void initRouter() {
        router = new Router(servers, configuration.getProperty(RealmGateSettings.FALLBACK_ENABLED), configuration.getProperty(RealmGateSettings.FALLBACK_ORDER));
    }

    public Map<String, BackendServer> getServers() {
        return Collections.unmodifiableMap(servers);
    }

}
