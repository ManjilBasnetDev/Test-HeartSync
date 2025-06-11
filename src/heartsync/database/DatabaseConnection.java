package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "heartsync";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "lokeshsingh9841@";
    private static Connection connection;
    private static DatabaseConnection instance;
    
    private DatabaseConnection() {
        initializeDatabase();
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try {
            // First try connecting to MySQL server
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                try (Statement stmt = conn.createStatement()) {
                    // Create database if it doesn't exist
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                    
                    // Use the database
                    stmt.executeUpdate("USE " + DB_NAME);
                    
                    // Create users table
                    String createUsersTable = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            username VARCHAR(50) UNIQUE NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            user_type ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
                            email VARCHAR(100),
                            phone_number VARCHAR(20),
                            date_of_birth DATE,
                            gender VARCHAR(10),
                            interests TEXT,
                            bio TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createUsersTable);
                    
                    // Create user_profiles table
                    String createProfilesTable = """
                        CREATE TABLE IF NOT EXISTS user_profiles (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            user_id INT NOT NULL,
                            full_name VARCHAR(100),
                            height INT,
                            weight INT,
                            country VARCHAR(100),
                            address TEXT,
                            phone VARCHAR(20),
                            qualification VARCHAR(50),
                            gender VARCHAR(20),
                            preferences VARCHAR(20),
                            about_me TEXT,
                            profile_pic_path VARCHAR(255),
                            relation_choice VARCHAR(50),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id)
                        )
                    """;
                    stmt.executeUpdate(createProfilesTable);
                    
                    // Create user_hobbies table
                    String createHobbiesTable = """
                        CREATE TABLE IF NOT EXISTS user_hobbies (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            user_id INT,
                            hobby VARCHAR(100),
                            FOREIGN KEY (user_id) REFERENCES users(id)
                        )
                    """;
                    stmt.executeUpdate(createHobbiesTable);
                    
                    // Insert test user if not exists
                    String insertTestUser = """
                        INSERT IGNORE INTO users (username, password, user_type, email)
                        VALUES ('testuser', ?, 'USER', 'test@example.com')
                    """;
                    try (var pstmt = conn.prepareStatement(insertTestUser)) {
                        pstmt.setString(1, hashPassword("test123"));
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            String error = "Database initialization error: " + e.getMessage();
            LOGGER.log(Level.SEVERE, error, e);
            JOptionPane.showMessageDialog(null,
                error,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
                LOGGER.info("Database connection established successfully");
            } catch (ClassNotFoundException e) {
                String error = "MySQL JDBC Driver not found";
                LOGGER.log(Level.SEVERE, error, e);
                throw new SQLException(error, e);
            } catch (SQLException e) {
                String error = "Failed to establish database connection";
                LOGGER.log(Level.SEVERE, error, e);
                throw e;
            }
        }
        return connection;
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password", e);
            return password; // Fallback to plain password if hashing fails
        }
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                LOGGER.info("Database connection closed successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
    
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            String error = "Database connection test failed: " + e.getMessage();
            LOGGER.log(Level.SEVERE, error, e);
            return false;
        }
    }
    
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
    }
    
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
} 