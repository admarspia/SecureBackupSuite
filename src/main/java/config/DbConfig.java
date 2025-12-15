package config;

import java.sql.*;

public class DbConfig {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }
}

