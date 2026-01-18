package com.realmgate.services.types.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.realmgate.RealmGatePlugin;
import com.realmgate.core.RealmGateCore;
import com.realmgate.services.Service;
import com.realmgate.services.Services;
import com.realmgate.services.types.config.validation.ConfigValidator;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class ConfigurationService implements Service {

    private final SettingsManager settings;
    private final ConfigValidator validator;

    public ConfigurationService() {
        settings = SettingsManagerBuilder
                .withYamlFile(Path.of("mods/RealmGate/config.yml"))
                .configurationData(RealmGateSettings.class)
                .useDefaultMigrationService()
                .create();
        validator = new ConfigValidator();
    }

    @Override
    public void init() {
    }

    @Override
    public void stop() {

    }
}
