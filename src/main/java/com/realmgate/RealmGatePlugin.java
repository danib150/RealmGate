package com.realmgate;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.realmgate.commands.ServerCommand;
import com.realmgate.config.validation.ConfigValidatorException;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.core.RealmGateCore;
import com.realmgate.services.Services;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.nio.file.Path;

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
        SettingsManager settings = SettingsManagerBuilder
                .withYamlFile(Path.of("mods/RealmGate/config.yml"))
                .configurationData(RealmGateSettings.class)
                .useDefaultMigrationService()
                .create();

        try {
            core = new RealmGateCore(settings);
        } catch (ConfigValidatorException e) {
            logger.atSevere().log("Failed to load RealmGate configuration: " + e.getMessage());
            return;
        }
        services = new Services(this);
        services.load();

        this.getCommandRegistry().registerCommand(new ServerCommand());

    }

    @Override
    public void shutdown() {
        if (services != null) services.unload();
    }
}
