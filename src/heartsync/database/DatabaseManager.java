package heartsync.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static DatabaseManager instance;
    private final DatabaseConnection dbConnection;

    private DatabaseManager() {
        dbConnection = DatabaseConnection.getInstance();
        initializeTables();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeTables() {
        try (Connection conn = dbConnection.getConnection()) {
            // Create users table
            try (Statement stmt = conn.createStatement()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }

    public int saveUserProfile(int userId, String fullName, int height, int weight, int age,
                               String country, String address, String phone, String qualification,
                             String gender, String preferences, String aboutMe, 
                             String profilePicPath, String relationChoice, 
                             List<String> hobbies) throws SQLException {
        String sql = """
            INSERT INTO user_profiles (user_id, full_name, height, weight, age, country, address,
                                     phone, qualification, gender, preferences, about_me,
                                     profile_pic_path, relation_choice)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, fullName);
            stmt.setInt(3, height);
            stmt.setInt(4, weight);
            stmt.setInt(5, age);
            stmt.setString(6, country);
            stmt.setString(7, address);
            stmt.setString(8, phone);
            stmt.setString(9, qualification);
            stmt.setString(10, gender);
            stmt.setString(11, preferences);
            stmt.setString(12, aboutMe);
            stmt.setString(13, profilePicPath);
            stmt.setString(14, relationChoice);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user profile failed, no rows affected.");
            }
            
            int profileId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profileId = generatedKeys.getInt(1);
                    } else {
                    throw new SQLException("Creating user profile failed, no ID obtained.");
                }
            }

            // Save hobbies
            if (hobbies != null && !hobbies.isEmpty()) {
                String hobbySQL = "INSERT INTO user_hobbies (user_id, hobby) VALUES (?, ?)";
                try (PreparedStatement hobbyStmt = conn.prepareStatement(hobbySQL)) {
                for (String hobby : hobbies) {
                        hobbyStmt.setInt(1, userId);
                        hobbyStmt.setString(2, hobby);
                        hobbyStmt.addBatch();
                }
                    hobbyStmt.executeBatch();
                }
            }

            LOGGER.log(Level.INFO, "Created user profile with ID: {0}", profileId);
            return profileId;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user profile", e);
            throw new SQLException("Failed to save user profile: " + e.getMessage(), e);
        }
    }

    public void closeConnection() {
        dbConnection.closeConnection();
    }
} 