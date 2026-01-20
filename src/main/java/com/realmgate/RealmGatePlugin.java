package com.realmgate;

import ch.jalu.configme.SettingsManager;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.io.ServerManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.realmgate.commands.ServerCommand;
import com.realmgate.commands.WhereCommand;
import com.realmgate.commands.WhoAmICommand;
import com.realmgate.server.BackendServer;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.services.types.config.validation.ConfigValidatorException;
import com.realmgate.core.RealmGateCore;
import com.realmgate.services.Services;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.net.SocketException;

@Getter
public class RealmGatePlugin extends JavaPlugin {

    @Getter
    private static RealmGatePlugin instance;
    private HytaleLogger logger = HytaleLogger.forEnclosingClass();
    private RealmGateCore core;
    private Services services;

    private static boolean enabled = true;


    public RealmGatePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    public void setup() {
        logger = HytaleLogger.get("RealmGate");

        services = new Services();
        services.load();

        try {
            core = new RealmGateCore(this);
        } catch (ConfigValidatorException e) {
            logger.atSevere().log("Failed to load RealmGate configuration: " + e.getMessage());
            enabled = false;
            return;
        }

        services.load(getTaskRegistry(), core.getServerRouter());

        this.getCommandRegistry().registerCommand(new ServerCommand());
        this.getCommandRegistry().registerCommand(new WhoAmICommand());
        this.getCommandRegistry().registerCommand(new WhereCommand());

    }

    @Override
    public void start() {
        if (!enabled) return;

        SettingsManager settings = services.getConfigurationService().getSettings();

        String serverName = settings.getProperty(RealmGateSettings.DEFAULT);
        String ip = settings.getProperty(RealmGateSettings.SERVER_ADDRESS);

        int port = settings.getProperty(RealmGateSettings.SERVER_PORT);
        if (port == 0) {
            InetSocketAddress bind = null;
            try {
                bind = ServerManager.get().getLocalOrPublicAddress();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            if (bind == null) {
                throw new IllegalStateException("Cannot resolve bind port");
            }
            port = bind.getPort();
        }

        services.getMysqlService().getRepository().createOrUpdateServer(new BackendServer(serverName, ip, port));

    }

    @Override
    public void shutdown() {
        if (services != null) services.unload();
    }


}
