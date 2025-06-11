package com.heartsync.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "config.properties";
    private static String url;
    private static String user;
    private static String password;
    private static Connection connection = null;
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "3306");
            String database = props.getProperty("db.name", "heartsync");
            user = props.getProperty("db.user", "root");
            password = props.getProperty("db.password", "");
            
            url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true", 
                              host, port, database);
        } catch (IOException e) {
            System.err.println("Warning: Could not load config.properties. Using default values.");
            url = "jdbc:mysql://localhost:3306/heartsync?useSSL=false&allowPublicKeyRetrieval=true";
            user = "root";
            password = "";
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
} 