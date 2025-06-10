package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "datingapp";
    private static final String USERNAME = "manjil";
    private static final String PASSWORD = "3023";
    private static Connection connection = null;
    private static DatabaseConnection instance = null;
    
    private DatabaseConnection() {
        initializeDatabase();
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    static {
        initializeDatabase();
    }
    
    private static void initializeDatabase() {
        try {
            // First try connecting to MySQL server
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                try (var stmt = conn.createStatement()) {
                    // Create database if it doesn't exist
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Database initialization failed: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
                System.out.println("Database connected successfully");
            } catch (ClassNotFoundException e) {
                System.err.println("Database driver not found: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                    "Database driver not found: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                    "Database connection failed: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    public boolean testConnection() {
        try {
            getConnection();
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
} 