package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ResetDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/heartsync";
    private static final String USER = "manjil";
    private static final String PASSWORD = "3023";  // Set your MySQL password here
    
    public static Connection getConnection() throws SQLException {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver loaded successfully");
            
            // Attempt to create the database if it doesn't exist
            try (Connection tempConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/", USER, PASSWORD)) {
                tempConn.createStatement().execute("CREATE DATABASE IF NOT EXISTS heartsync");
                System.out.println("Database 'heartsync' created or already exists");
            }
            
            // Connect to the specific database
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Successfully connected to database");
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        }
    }
    
    // Initialize database tables
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            System.out.println("Starting database initialization...");
            createTables(conn);
            System.out.println("Database initialization completed successfully");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    private static void createTables(Connection conn) throws SQLException {
        try (var stmt = conn.createStatement()) {
            System.out.println("Creating tables if they don't exist...");
            
            // Create users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);
            System.out.println("Users table created or verified");
            
            // Create user_profiles table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_profiles (
                    user_id INT PRIMARY KEY,
                    first_name VARCHAR(50),
                    last_name VARCHAR(50),
                    date_of_birth DATE,
                    gender VARCHAR(10),
                    profile_picture VARCHAR(255),
                    bio TEXT,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
            System.out.println("User profiles table created or verified");
            
            // Create health_data table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS health_data (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT,
                    heart_rate INT,
                    blood_pressure_systolic INT,
                    blood_pressure_diastolic INT,
                    measurement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    notes TEXT,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
            System.out.println("Health data table created or verified");
            
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw e;
        }
    }
    
    // Verify database connection
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
} 