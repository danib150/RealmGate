package com.realmgate.core;

import ch.jalu.configme.SettingsManager;
import com.realmgate.RealmGatePlugin;
import com.realmgate.security.PayloadSigner;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.services.types.config.validation.ConfigValidatorException;
import com.realmgate.server.ServerRouter;
import com.realmgate.server.BackendServer;
import com.realmgate.services.types.mysql.ServerRepository;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RealmGateCore {

    private final RealmGatePlugin plugin;
    private final SettingsManager settings;
    private final ServerRepository serverRepository;

    @Getter
    private ServerRouter serverRouter;

    private Map<String, BackendServer> servers = new HashMap<>();

    @Getter
    private PayloadSigner payloadSigner;

    public RealmGateCore(RealmGatePlugin plugin) throws ConfigValidatorException {
        this.plugin = plugin;
        settings = plugin.getServices().getConfigurationService().getSettings();
        serverRepository = plugin.getServices().getMysqlService().getRepository();
        reload();
    }

    public void reload() throws ConfigValidatorException {
        settings.reload();
        payloadSigner = new PayloadSigner(settings.getProperty(RealmGateSettings.SECRET_KEY));
        loadServers();
        plugin.getServices().getConfigurationService().getValidator().validate(settings, servers);
        initRouter();
    }

    private void loadServers() {
        servers.clear();
        for (BackendServer server : serverRepository.findAll()) {
            servers.put(server.getName(), server);
        }
    }

    private void initRouter() {
        SettingsManager settingsManager = plugin.getServices().getConfigurationService().getSettings();
        serverRouter = new ServerRouter(servers,settingsManager.getProperty(RealmGateSettings.FALLBACK_ENABLED), settingsManager.getProperty(RealmGateSettings.FALLBACK_LIST), plugin.getServices().getRedisService());
    }

    public Map<String, BackendServer> getServers() {
        return Collections.unmodifiableMap(servers);
    }

}
