package fr.romitou.balkourabattle;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaController {

    private final String url;
    private final String hostname;
    private final String database;
    private final String user;
    private final String password;
    private Connection connection;

    public MariaController(String url, String hostname, String database, String user, String password) {
        this.url = url;
        this.hostname = hostname;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url + hostname + "/" + database + "?autoReconnect=true&allowMultiQueries=true&characterEncoding=UTF-8", user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getLogger().info("[BalkouraBattle] SQL connecté.");
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
