package config;
import java.sql.*;

public class DbConfig{
    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);

    } 

}


