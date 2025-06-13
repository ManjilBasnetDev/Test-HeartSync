package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

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
    
    /**
     * Hashes a password using SHA-256 algorithm.
     * @param password The password to hash
     * @return The hashed password as a hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Validates an email address format.
     * @param email The email address to validate
     * @throws IllegalArgumentException if the email format is invalid
     */
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    /**
     * Validates password requirements.
     * @param password The password to validate
     * @throws IllegalArgumentException if the password does not meet requirements
     */
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        
        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        
        // Check for at least one number
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        
        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
} 