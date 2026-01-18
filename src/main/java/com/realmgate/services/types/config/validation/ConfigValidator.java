package com.realmgate.services.types.config.validation;

import ch.jalu.configme.SettingsManager;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.server.BackendServer;

import java.util.List;
import java.util.Map;

public class ConfigValidator {

    public void validate(SettingsManager settings, Map<String, BackendServer> servers) throws ConfigValidatorException {
        validateCore(settings, servers);
        validateFallback(settings, servers);
        validateMySQL(settings);
        validateRedis(settings);
    }

    private void validateCore(SettingsManager settings, Map<String, BackendServer> servers) throws ConfigValidatorException {

        String secret = settings.getProperty(RealmGateSettings.SECRET_KEY);
        if (secret.isBlank() || secret.equals("CHANGE_ME")) {
            throw new ConfigValidatorException("secret-key must be set and must not be CHANGE_ME");
        }

        String def = settings.getProperty(RealmGateSettings.DEFAULT);
        if (def.isBlank()) {
            throw new ConfigValidatorException("default-server cannot be empty");
        }

        if (!servers.containsKey(def)) {
            throw new ConfigValidatorException("default-server references unknown server: " + def);
        }
    }


    private void validateFallback(SettingsManager settings, Map<String, BackendServer> servers) throws ConfigValidatorException {

        boolean enabled = settings.getProperty(RealmGateSettings.FALLBACK_ENABLED);

        List<String> list = settings.getProperty(RealmGateSettings.FALLBACK_LIST);

        if (!enabled) {
            return;
        }

        if (list.isEmpty()) {
            throw new ConfigValidatorException("fallback.enabled is true but fallback.list is empty");
        }

        for (String name : list) {
            if (!servers.containsKey(name)) {
                throw new ConfigValidatorException("fallback.list references unknown server: " + name);
            }
        }
    }



    private void validateMySQL(SettingsManager settings) throws ConfigValidatorException {

        if (settings.getProperty(RealmGateSettings.MYSQL_HOST).isBlank()) {
            throw new ConfigValidatorException("mysql.host cannot be empty");
        }

        int port = settings.getProperty(RealmGateSettings.MYSQL_PORT);
        if (port <= 0 || port > 65535) {
            throw new ConfigValidatorException("mysql.port out of range");
        }

        if (settings.getProperty(RealmGateSettings.MYSQL_DATABASE).isBlank()) {
            throw new ConfigValidatorException("mysql.database cannot be empty");
        }

        if (settings.getProperty(RealmGateSettings.MYSQL_USERNAME).isBlank()) {
            throw new ConfigValidatorException("mysql.username cannot be empty");
        }
    }

    private void validateRedis(SettingsManager settings) throws ConfigValidatorException {

        if (settings.getProperty(RealmGateSettings.REDIS_HOST).isBlank()) {
            throw new ConfigValidatorException("redis.host cannot be empty");
        }

        int port = settings.getProperty(RealmGateSettings.REDIS_PORT);
        if (port <= 0 || port > 65535) {
            throw new ConfigValidatorException("redis.port out of range");
        }

        int ttl = settings.getProperty(RealmGateSettings.REDIS_HEARTBEAT_TTL);
        if (ttl <= 0) {
            throw new ConfigValidatorException("redis.heartbeat-ttl must be > 0");
        }
    }
}
