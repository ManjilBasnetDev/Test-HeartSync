package heartsync.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
    
    public static void initializeDatabase() {
        try (Connection conn = MySqlConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
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
            
            // Create messages table
            String createMessagesTable = """
                CREATE TABLE IF NOT EXISTS messages (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    sender_id INT NOT NULL,
                    receiver_id INT NOT NULL,
                    message_text TEXT NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (sender_id) REFERENCES users(id),
                    FOREIGN KEY (receiver_id) REFERENCES users(id)
                )
            """;
            stmt.executeUpdate(createMessagesTable);
            
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
                pstmt.setString(1, MySqlConnection.hashPassword("test123"));
                pstmt.executeUpdate();
            }
            
            LOGGER.info("Database schema initialized successfully");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database schema", e);
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
} 