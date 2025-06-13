package heartsyncdatingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProfileSetup {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnectionProfileSetup() {
        try {
            // Register JDBC driver
            Class.forName(DatabaseConfigSetup.DRIVER_CLASS);
            
            // Verify driver is loaded
            try {
                DriverManager.getDriver(DatabaseConfigSetup.DB_URL);
            } catch (SQLException e) {
                System.err.println("Driver not found for URL: " + DatabaseConfigSetup.DB_URL);
                throw new RuntimeException("MySQL JDBC driver not properly registered", e);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC driver: " + e.getMessage());
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(
                    DatabaseConfigSetup.DB_URL,
                    DatabaseConfigSetup.USER,
                    DatabaseConfigSetup.PASS
                );
            } catch (SQLException e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
}