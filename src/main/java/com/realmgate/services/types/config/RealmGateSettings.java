package com.realmgate.services.types.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.ListProperty;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public final class RealmGateSettings implements SettingsHolder {

    /* ------------------------
     * Core
     * ------------------------ */

    @Comment("Secret key used to sign and verify referral data between servers")
    public static final Property<String> SECRET_KEY = newProperty("secret-key", "CHANGE_ME");

    @Comment("Redirect-Only Enabled (The server try to redirect you to an another server skipping this server)")
    public static final Property<Boolean> REDIRECT_ONLY = newProperty("redirect-only", false);

    @Comment("Default server name used when redirect-only or fallback is enabled")
    public static final Property<String> DEFAULT = newProperty("default-server", "lobby");

    @Comment("Enable fallback routing if the default server is unavailable")
    public static final Property<Boolean> FALLBACK_ENABLED = newProperty("fallback.enabled", false);

    @Comment("Fallback list")
    public static final ListProperty<String> FALLBACK_LIST = newListProperty("fallback.list", "lobby");

    /* ------------------------
     * MySQL
     * ------------------------ */

    @Comment("MySQL hostname")
    public static final Property<String> MYSQL_HOST = newProperty("mysql.host", "127.0.0.1");

    @Comment("MySQL port")
    public static final Property<Integer> MYSQL_PORT = newProperty("mysql.port", 3306);

    @Comment("MySQL database name")
    public static final Property<String> MYSQL_DATABASE = newProperty("mysql.database", "realm_gate");

    @Comment("MySQL username")
    public static final Property<String> MYSQL_USERNAME = newProperty("mysql.username", "realm_gate");

    @Comment("MySQL password")
    public static final Property<String> MYSQL_PASSWORD = newProperty("mysql.password", "password");

    @Comment("Use SSL for MySQL connection")
    public static final Property<Boolean> MYSQL_USE_SSL = newProperty("mysql.use-ssl", false);


    @Comment("Redis hostname")
    public static final Property<String> REDIS_HOST = newProperty("redis.host", "127.0.0.1");

    @Comment("Redis port")
    public static final Property<Integer> REDIS_PORT = newProperty("redis.port", 6379);

    @Comment("Redis password (leave empty if none)")
    public static final Property<String> REDIS_PASSWORD = newProperty("redis.password", "");

    @Comment("Redis database index")
    public static final Property<Integer> REDIS_DATABASE = newProperty("redis.database", 0);

    @Comment("Redis connection timeout in milliseconds")
    public static final Property<Integer> REDIS_TIMEOUT_MS = newProperty("redis.timeout-ms", 200);

    @Comment("TTL (seconds) for server heartbeat keys")
    public static final Property<Integer> REDIS_HEARTBEAT_TTL = newProperty("redis.heartbeat-ttl", 10);
}
