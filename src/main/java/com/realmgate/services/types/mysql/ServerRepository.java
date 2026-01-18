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
    }

    public void createServer(BackendServer server) {
        String sql = "INSERT INTO servers (name, host, port) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, server.getName());
            ps.setString(2, server.getAddress());
            ps.setInt(3, server.getPort());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to create server " + server.getName(), e
            );
        }
    }


    public List<BackendServer> findAll() {
        String sql = "SELECT name, host, port FROM servers";

        List<BackendServer> servers = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, server.getName());
            ps.setString(2, server.getAddress());
            ps.setInt(3, server.getPort());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to create or update server " + server.getName(), e
            );
        }
    }


    private BackendServer map(ResultSet rs) throws SQLException {
        return new BackendServer(
                rs.getString("name"),
                rs.getString("host"),
                rs.getInt("port")
        );
    }

}
