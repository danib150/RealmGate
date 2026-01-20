package com.realmgate.services.types.config.validation;

import ch.jalu.configme.SettingsManager;
import com.hypixel.hytale.server.core.io.ServerManager;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.server.BackendServer;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

public class ConfigValidator {

    public void validate(SettingsManager settings) throws ConfigValidatorException {
        validateCore(settings);
        validateMySQL(settings);
        validateRedis(settings);
    }

    private void validateCore(SettingsManager settings) throws ConfigValidatorException {

        String secret = settings.getProperty(RealmGateSettings.SECRET_KEY);
        if (secret.isBlank() || secret.equals("CHANGE_ME")) {
            throw new ConfigValidatorException("secret-key must be set and must not be CHANGE_ME");
        }

        String def = settings.getProperty(RealmGateSettings.DEFAULT);
        if (def.isBlank()) {
            throw new ConfigValidatorException("default-server cannot be empty");
        }

        String address = settings.getProperty(RealmGateSettings.SERVER_ADDRESS);
        if (address.isBlank()) {
            throw new ConfigValidatorException("server-public-address cannot be empty");
        }

        int port = settings.getProperty(RealmGateSettings.SERVER_PORT);
        if (port < 0 || port > 65535) {
            throw new ConfigValidatorException("server-public-port out of range");
        }


        /*
        if (!servers.containsKey(def)) {
            throw new ConfigValidatorException("default-server references unknown server: " + def);
        }

         */
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
