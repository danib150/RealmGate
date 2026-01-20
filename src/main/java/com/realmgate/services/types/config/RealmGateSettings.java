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

    @Comment("Logical name of this server in the network (e.g. lobby-1, minigame-2). "
            + "Must be unique and match the entry stored in the database.")
    public static final Property<String> DEFAULT = newProperty("server-name", "lobby");

    @Comment("Public address used by other servers or gateways to connect to this server. "
            + "This should be a hostname or public IP (not 0.0.0.0 or a Docker internal IP). "
            + "Example: play.mynetwork.com")
    public static final Property<String> SERVER_ADDRESS = newProperty("server-public-address", "");

    @Comment("Port used by other servers or gateways to connect. "
            + "If set to 0, the runtime bound port will be used.")
    public static final Property<Integer> SERVER_PORT = newProperty("server-public-port", 0);

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
