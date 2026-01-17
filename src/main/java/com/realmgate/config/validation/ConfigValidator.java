package com.realmgate.config.validation;

import ch.jalu.configme.SettingsManager;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.server.BackendServer;

import java.util.List;
import java.util.Map;

public final class ConfigValidator {

    public static void validate(SettingsManager settings, Map<String, BackendServer> servers) {

        if (servers.isEmpty()) {
            throw new ConfigValidatorException("No backend servers configured");
        }

        String defaultServer =
                settings.getProperty(RealmGateSettings.DEFAULT_SERVER);

        if (!servers.containsKey(defaultServer)) {
            throw new ConfigValidatorException(
                    "Default server '" + defaultServer + "' does not exist"
            );
        }

        boolean fallbackEnabled =
                settings.getProperty(RealmGateSettings.FALLBACK_ENABLED);

        if (fallbackEnabled) {
            List<String> fallbackOrder =
                    settings.getProperty(RealmGateSettings.FALLBACK_ORDER);

            if (fallbackOrder.isEmpty()) {
                throw new ConfigValidatorException("Fallback is enabled but fallback order is empty"
                );
            }

            for (String server : fallbackOrder) {
                if (!servers.containsKey(server)) {
                    throw new ConfigValidatorException("Fallback server '" + server + "' does not exist");
                }
            }
        }
    }
}
