package com.realmgate.services.types.mysql;

import ch.jalu.configme.SettingsManager;
import com.realmgate.server.BackendServer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerRepository {

    private final DataSource dataSource;

    public ServerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfNotExists();
    }

    public boolean delete(String name) {
        String sql = "DELETE FROM servers WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to delete server: " + name, e
            );
        }
    }

    public boolean exists(String name) {
        String sql = "SELECT 1 FROM servers WHERE name = ? LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check server existence: " + name, e);
        }
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS servers (id INT UNSIGNED NOT NULL AUTO_INCREMENT, name VARCHAR(64) NOT NULL, host VARCHAR(255) NOT NULL, port INT UNSIGNED NOT NULL, PRIMARY KEY (id), UNIQUE KEY uk_servers_name (name)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.execute();

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create servers table", e);
        }
    }


    public void createServer(BackendServer server) {
        String sql = "INSERT INTO servers (name, host, port) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, server.getName());
            ps.setString(2, server.getAddress());
            ps.setInt(3, server.getPort());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create server " + server.getName(), e);
        }
    }


    public List<BackendServer> findAll() {
        String sql = "SELECT name, host, port FROM servers";

        List<BackendServer> servers = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servers.add(map(rs));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load servers", e);
        }

        return servers;
    }

    public void createOrUpdateServer(BackendServer server) {
        String sql = "INSERT INTO servers (name, host, port) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE host = VALUES(host), port = VALUES(port)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, server.getName());
            ps.setString(2, server.getAddress());
            ps.setInt(3, server.getPort());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create or update server " + server.getName(), e);
        }
    }


    private BackendServer map(ResultSet rs) throws SQLException {
        return new BackendServer(rs.getString("name"), rs.getString("host"), rs.getInt("port"));
    }

}
