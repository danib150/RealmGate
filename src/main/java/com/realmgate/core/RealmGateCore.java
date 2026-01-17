package com.realmgate.core;

import ch.jalu.configme.SettingsManager;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.config.ServerConfigLoader;
import com.realmgate.config.validation.ConfigValidator;
import com.realmgate.config.validation.ConfigValidatorException;
import com.realmgate.server.ServerRouter;
import com.realmgate.server.BackendServer;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RealmGateCore {

    private final SettingsManager configuration;
    private ServerRouter serverRouter;
    private final ServerConfigLoader serverConfigLoader;

    private final Map<String, BackendServer> servers = new HashMap<>();

    public RealmGateCore(SettingsManager configuration) throws ConfigValidatorException {
        this.configuration = configuration;
        this.serverConfigLoader = new ServerConfigLoader(configuration);
        reload();
    }

    public void reload() throws ConfigValidatorException {
        configuration.reload();

        Map<String, BackendServer> loaded = serverConfigLoader.getBackendServers();

        servers.clear();
        servers.putAll(loaded);

        ConfigValidator.validate(configuration, servers);

        initRouter();
    }

    private void initRouter() {
        serverRouter = new ServerRouter(servers, configuration.getProperty(RealmGateSettings.FALLBACK_ENABLED), configuration.getProperty(RealmGateSettings.FALLBACK_ORDER));
    }

    public Map<String, BackendServer> getServers() {
        return Collections.unmodifiableMap(servers);
    }

}
