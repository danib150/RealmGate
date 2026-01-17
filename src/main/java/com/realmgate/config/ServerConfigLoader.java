package com.realmgate.config;

import ch.jalu.configme.SettingsManager;
import com.realmgate.RealmGatePlugin;
import com.realmgate.server.BackendServer;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerConfigLoader {

    @Getter private SettingsManager settings;

    public ServerConfigLoader(SettingsManager settings) {
            this.settings = settings;
    }


    /**
     * Loads backend servers from the "servers" section of config.yml.
     *
     * @return map of server name -> BackendServer
     */
    public Map<String, BackendServer> getBackendServers() {
        Map<String, BackendServer> servers = new HashMap<>();
        List<String> entries = settings.getProperty(RealmGateSettings.SERVER_LIST);

        for (String entry : entries) {
            String[] nameSplit = entry.split("=", 2);
            if (nameSplit.length != 2) {
                RealmGatePlugin.getInstance().getLogger().atWarning().log("Invalid server entry '%s' (expected name=ip:port)", entry);
                continue;
            }

            String name = nameSplit[0];
            String addressPart = nameSplit[1];

            String[] addressSplit = addressPart.split(":", 2);
            if (addressSplit.length != 2) {
                RealmGatePlugin.getInstance().getLogger().atWarning().log("Invalid address for server '%s' (expected ip:port)", name);
                continue;
            }

            String host = addressSplit[0];
            int port;

            try {
                port = Integer.parseInt(addressSplit[1]);
            } catch (NumberFormatException e) {
                RealmGatePlugin.getInstance().getLogger().atWarning().log("Invalid port for server '%s': %s", name, addressSplit[1]);
                continue;
            }

            if (port <= 0 || port > 65535) {
                RealmGatePlugin.getInstance().getLogger().atWarning().log("Port out of range for server '%s': %d", name, port);
                continue;
            }

            servers.put(name, new BackendServer(name, host, port));
        }

        return servers;
    }
}
