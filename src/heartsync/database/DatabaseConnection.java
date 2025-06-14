package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Unified database connection manager for the HeartSync Dating App.
 * Handles all database operations including initialization and connection management.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "HEARTSYNCFINAL";
    private static final String USERNAME = "manjil";
    private static final String PASSWORD = "3023";
    private static boolean driverLoaded = false;
    private static Connection connection = null;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MINIMUM_AGE = 18;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("MySQL JDBC Driver not loaded");
        }
        
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
        }
        
        return connection;
    }
    
    private static void initializeDatabase() throws SQLException {
        if (!driverLoaded) {
            return;
        }
        
        try {
            // First try connecting to MySQL server
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                try (Statement stmt = conn.createStatement()) {
                    // Create database if it doesn't exist
                    
                    stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
                    
                    // Use the database
                    stmt.executeUpdate("USE " + DB_NAME);
                    
                    // Create users table - main user authentication and basic info
                    String createUsersTableSQL = """
                        CREATE TABLE users (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            user_type ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
                            email VARCHAR(100) UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createUsersTableSQL);
                    
                    // Create user_profiles table - detailed user information
                    String createUserProfilesTableSQL = """
                        CREATE TABLE user_profiles (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT NOT NULL UNIQUE,
                            full_name VARCHAR(100) NOT NULL,
                            height INT NOT NULL,
                            weight INT NOT NULL,
                            country VARCHAR(50) NOT NULL,
                            address VARCHAR(200) NOT NULL,
                            phone VARCHAR(20) NOT NULL,
                            qualification VARCHAR(100) NOT NULL,
                            gender VARCHAR(20) NOT NULL,
                            preferences VARCHAR(20) NOT NULL,
                            about_me TEXT NOT NULL,
                            profile_pic_path VARCHAR(500),
                            relation_choice VARCHAR(50) NOT NULL,
                            date_of_birth DATE,
                            interests TEXT,
                            bio TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createUserProfilesTableSQL);
                    
                    // Create user_hobbies table
                    String createHobbiesTableSQL = """
                        CREATE TABLE user_hobbies (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT NOT NULL,
                            hobby VARCHAR(100) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createHobbiesTableSQL);
                    
                    // Create contacts table for contact form submissions
                    String createContactsTableSQL = """
                        CREATE TABLE contacts (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            full_name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            phone_number VARCHAR(20),
                            subject VARCHAR(200),
                            message TEXT NOT NULL,
                            status ENUM('NEW', 'READ', 'REPLIED') DEFAULT 'NEW',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        )
                    """;
                    stmt.executeUpdate(createContactsTableSQL);
                    
                    // Create matches table for user matches
                    String createMatchesTableSQL = """
                        CREATE TABLE matches (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user1_id INT NOT NULL,
                            user2_id INT NOT NULL,
                            match_status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createMatchesTableSQL);
                    
                    // Create messages table for user communication
                    String createMessagesTableSQL = """
                        CREATE TABLE messages (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            sender_id INT NOT NULL,
                            receiver_id INT NOT NULL,
                            message_text TEXT NOT NULL,
                            message_type ENUM('TEXT', 'IMAGE', 'FILE') DEFAULT 'TEXT',
                            attachment_path VARCHAR(255),
                            is_read BOOLEAN DEFAULT FALSE,
                            sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            read_at TIMESTAMP NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
                    stmt.executeUpdate(createMessagesTableSQL);

                    // Insert default admin user
                    String insertAdminSQL = """
                        INSERT INTO users (username, password, user_type, email)
                        VALUES ('admin', 'admin123', 'ADMIN', 'admin@heartsync.com')
                    """;
                    stmt.executeUpdate(insertAdminSQL);
                    
                    System.out.println("Database and tables initialized successfully");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw e;
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(5); // 5 second timeout
        } catch (SQLException e) {
            String error = "Database connection test failed: " + e.getMessage();
            System.err.println(error);
            e.printStackTrace();
            return false;
        }
    }
    
    public static void validateAge(String dateOfBirth) {
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
            LocalDate now = LocalDate.now();
            int age = Period.between(dob, now).getYears();
            
            if (age < MINIMUM_AGE) {
                throw new IllegalArgumentException("You must be at least " + MINIMUM_AGE + " years old to register.");
            }
            if (dob.isAfter(now)) {
                throw new IllegalArgumentException("Date of birth cannot be in the future.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format.");
        }
    }
    
    public static LocalDate parseAndValidateDate(String dateOfBirth) throws IllegalArgumentException {
        validateAge(dateOfBirth);
        return LocalDate.parse(dateOfBirth, DATE_FORMATTER);
    }
    
    public static int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}