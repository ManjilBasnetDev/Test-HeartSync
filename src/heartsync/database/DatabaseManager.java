package heartsync.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseManager {
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

    public int saveUserProfile(int userId, String fullName, int height, int weight, String country, 
                             String address, String phone, String qualification, 
                             String gender, String preferences, String aboutMe, 
                             String profilePicPath, String relationChoice, 
                             List<String> hobbies) throws SQLException {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert user profile
            String insertProfileSQL = """
                INSERT INTO user_profiles (user_id, full_name, height, weight, country, 
                                        address, phone, qualification, gender, preferences, 
                                        about_me, profile_pic_path, relation_choice)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            int profileId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertProfileSQL, Statement.RETURN_GENERATED_KEYS)) {
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

                // Get the generated profile ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        profileId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get profile ID");
                    }
                }
            }

            // Insert hobbies
            if (hobbies != null && !hobbies.isEmpty()) {
                String insertHobbySQL = "INSERT INTO user_hobbies (user_id, hobby) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertHobbySQL)) {
                    for (String hobby : hobbies) {
                        pstmt.setInt(1, userId);
                        pstmt.setString(2, hobby);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            conn.commit();
            return profileId;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeConnection() {
        dbConnection.closeConnection();
    }
} 