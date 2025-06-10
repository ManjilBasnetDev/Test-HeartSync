package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection1 {
    private static DatabaseConnection1 instance;
    private Connection connection;
    
    private DatabaseConnection1() {
        try {
            // Register JDBC driver
            Class.forName(DatabaseConfig.DRIVER_CLASS);
            
            // Verify driver is loaded
            try {
                DriverManager.getDriver(DatabaseConfig.DB_URL);
            } catch (SQLException e) {
                System.err.println("Driver not found for URL: " + DatabaseConfig.DB_URL);
                throw new RuntimeException("MySQL JDBC driver not properly registered", e);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC driver: " + e.getMessage());
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }
    
    public static DatabaseConnection1 getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection1();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(
                    DatabaseConfig.DB_URL,
                    DatabaseConfig.USER,
                    DatabaseConfig.PASS
                );
            } catch (SQLException e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
} 