package com.realmgate.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;

public final class RealmGateSettings implements SettingsHolder {

    @Comment("Enable or disable RealmGate")
    public static final Property<Boolean> ENABLED = newProperty("enabled", true);

    @Comment("Default server where players are sent on join")
    public static final Property<String> DEFAULT_SERVER = newProperty("default-server", "lobby");

    @Comment("Ordered list of fallback servers")
    public static final Property<List<String>> SERVER_LIST = newListProperty("server-list.order", List.of("lobby=127.0.0.1:5522"));


    /* ======================================================
     * FALLBACK
     * ====================================================== */

    @Comment("Enable fallback routing if a server is unavailable")
    public static final Property<Boolean> FALLBACK_ENABLED = newProperty("fallback.enabled", true);

    @Comment("Ordered list of fallback servers")
    public static final Property<List<String>> FALLBACK_ORDER = newListProperty("fallback.order", List.of("lobby"));

    /* ======================================================
     * CONNECTION
     * ====================================================== */

    @Comment("Connection timeout in milliseconds")
    public static final Property<Integer> CONNECT_TIMEOUT_MS = newProperty("connection.timeout-ms", 3000);

    @Comment("Delay (in ticks) before sending a player to a server on join")
    public static final Property<Integer> JOIN_DELAY_TICKS = newProperty("connection.join-delay-ticks", 0);

    /* ======================================================
     * DEBUG
     * ====================================================== */

    @Comment("Enable debug logging")
    public static final Property<Boolean> DEBUG = newProperty("debug", false);
}
