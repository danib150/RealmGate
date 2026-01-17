package com.realmgate.services.types.mysql;

import ch.jalu.configme.SettingsManager;
import com.realmgate.config.RealmGateSettings;
import com.realmgate.services.Service;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class MySQLService implements Service {

    private final SettingsManager settings;
    private HikariDataSource dataSource;

    public MySQLService(SettingsManager settings) {
        this.settings = settings;
    }

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();

        String host = settings.getProperty(RealmGateSettings.MYSQL_HOST);
        int port = settings.getProperty(RealmGateSettings.MYSQL_PORT);
        String database = settings.getProperty(RealmGateSettings.MYSQL_DATABASE);

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        config.setUsername(settings.getProperty(RealmGateSettings.MYSQL_USERNAME));
        config.setPassword(settings.getProperty(RealmGateSettings.MYSQL_PASSWORD));

        config.setMaximumPoolSize(1);
        config.setMinimumIdle(1);
        config.setPoolName("RealmGate-MySQL");

        config.addDataSourceProperty("useSSL", settings.getProperty(RealmGateSettings.MYSQL_USE_SSL));
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);

        this.dataSource = new HikariDataSource(config);
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
