package com.realmgate;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.realmgate.commands.ServerCommand;
import com.realmgate.services.types.config.validation.ConfigValidatorException;
import com.realmgate.core.RealmGateCore;
import com.realmgate.services.Services;
import lombok.Getter;

import javax.annotation.Nonnull;

@Getter
public class RealmGatePlugin extends JavaPlugin {

    @Getter
    private static RealmGatePlugin instance;
    private HytaleLogger logger = HytaleLogger.forEnclosingClass();
    private RealmGateCore core;
    private Services services;


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
            return;
        }

        this.getCommandRegistry().registerCommand(new ServerCommand());

    }

    @Override
    public void shutdown() {
        if (services != null) services.unload();
    }
}
