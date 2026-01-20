package com.realmgate.services.types.mysql;

import ch.jalu.configme.SettingsManager;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.services.Service;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;

public class MySQLService implements Service {

    private final SettingsManager settings;
    private HikariDataSource dataSource;

    @Getter private ServerRepository repository;

    public MySQLService(SettingsManager settings) {
        this.settings = settings;
    }

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();

        String host = settings.getProperty(RealmGateSettings.MYSQL_HOST);
        int port = settings.getProperty(RealmGateSettings.MYSQL_PORT);
        String database = settings.getProperty(RealmGateSettings.MYSQL_DATABASE);
        boolean useSsl = settings.getProperty(RealmGateSettings.MYSQL_USE_SSL);

        String jdbcUrl = "jdbc:mariadb://" + host + ":" + port + "/" + database + "?useSsl=" + useSsl + "&sslMode=" + (useSsl ? "REQUIRED" : "DISABLE");

        config.setJdbcUrl(jdbcUrl);

        config.setUsername(settings.getProperty(RealmGateSettings.MYSQL_USERNAME));
        config.setPassword(settings.getProperty(RealmGateSettings.MYSQL_PASSWORD));

        config.setDriverClassName("org.mariadb.jdbc.Driver");

        config.setMaximumPoolSize(1);
        config.setMinimumIdle(1);
        config.setPoolName("RealmGate-MariaDB");

        this.dataSource = new HikariDataSource(config);
        repository = new ServerRepository(dataSource);
    }

    @Override
    public void stop() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
