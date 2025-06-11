package heartsync.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager handles database schema initialization and management.
 * It follows the singleton pattern to ensure only one instance manages the database.
 */
public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static DatabaseManager instance;
    private final Connection connection;

    private DatabaseManager() {
        try {
            this.connection = DatabaseConnection.getConnection();
            if (this.connection == null) {
                throw new SQLException("Could not establish database connection");
            }
            initializeTables();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize DatabaseManager", e);
            throw new RuntimeException("Failed to initialize DatabaseManager", e);
        }
    }

    public static DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create users table with security questions
            stmt.execute("""
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
                    favorite_color VARCHAR(50),
                    first_school VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);
            
            // Create user_profiles table
            stmt.execute("""
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
            """);
            
            // Create user_hobbies table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_hobbies (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT,
                    hobby VARCHAR(100),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """);
            
            // Create contacts table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS contacts (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    full_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    message TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Create notifications table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS notifications (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    message TEXT NOT NULL,
                    type VARCHAR(50) NOT NULL,
                    is_read BOOLEAN DEFAULT FALSE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """);
            
            LOGGER.info("Database tables initialized successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error initializing database tables", e);
            throw e;
        }
    }

    public int saveUserProfile(int userId, String fullName, int height, int weight, String country, 
                             String address, String phone, String qualification, 
                             String gender, String preferences, String aboutMe, 
                             String profilePicPath, String relationChoice, 
                             List<String> hobbies) throws SQLException {
        try {
            connection.setAutoCommit(false);

            // Insert user profile
            String insertProfileSQL = """
                INSERT INTO user_profiles (user_id, full_name, height, weight, country, 
                                        address, phone, qualification, gender, preferences, 
                                        about_me, profile_pic_path, relation_choice)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            int profileId;
            try (PreparedStatement pstmt = connection.prepareStatement(insertProfileSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, fullName);
                pstmt.setInt(3, height);
                pstmt.setInt(4, weight);
                pstmt.setString(5, country);
                pstmt.setString(6, address);
                pstmt.setString(7, phone);
                pstmt.setString(8, qualification);
                pstmt.setString(9, gender);
                pstmt.setString(10, preferences);
                pstmt.setString(11, aboutMe);
                pstmt.setString(12, profilePicPath);
                pstmt.setString(13, relationChoice);

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        profileId = rs.getInt(1);
                    } else {
                        throw new SQLException("Creating user profile failed, no ID obtained.");
                    }
                }
            }

            // Insert hobbies
            if (hobbies != null && !hobbies.isEmpty()) {
                String insertHobbySQL = "INSERT INTO user_hobbies (user_id, hobby) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertHobbySQL)) {
                    for (String hobby : hobbies) {
                        pstmt.setInt(1, userId);
                        pstmt.setString(2, hobby);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            connection.commit();
            return profileId;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user profile", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error resetting auto-commit", e);
                }
            }
        }
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database connection", e);
        }
    }
}