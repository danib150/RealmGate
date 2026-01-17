package com.realmgate;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.core.RealmGateCore;
import com.realmgate.routing.Router;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.nio.file.Path;

@Getter
public class RealmGatePlugin extends JavaPlugin {

    @Getter
    private static RealmGatePlugin instance;
    private HytaleLogger logger = HytaleLogger.forEnclosingClass();
    private RealmGateCore core;


    public RealmGatePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    public void setup() {
        logger = HytaleLogger.get("RealmGate");
        SettingsManager settings = SettingsManagerBuilder
                .withYamlFile(Path.of("RealmGate/config.yml"))
                .configurationData(RealmGateSettings.class)
                .useDefaultMigrationService()
                .create();


        core = new RealmGateCore(settings);
    }
}
