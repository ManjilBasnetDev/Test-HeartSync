package heartsync.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseManagerProfile {
    private static DatabaseManagerProfile instance;

    private DatabaseManagerProfile() {
        initializeTables();
    }

    public static DatabaseManagerProfile getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerProfile();
        }
        return instance;
    }

    private void dropExistingTables() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Drop tables in reverse order of dependencies
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS user_hobbies");
                stmt.execute("DROP TABLE IF EXISTS user_profiles");
            }
        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
            throw new RuntimeException("Failed to drop tables", e);
        }
    }

    private void initializeTables() {
        // Drop existing tables first
        dropExistingTables();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Create user_profiles table with all required fields
            try (PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS user_profiles (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL UNIQUE, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "height INT NOT NULL, " +
                "age INT, " +
                "weight INT NOT NULL, " +
                "country VARCHAR(50) NOT NULL, " +
                "address VARCHAR(200) NOT NULL, " +
                "phone VARCHAR(20) NOT NULL, " +
                "qualification VARCHAR(100) NOT NULL, " +
                "gender VARCHAR(20) NOT NULL, " +
                "preferences VARCHAR(20) NOT NULL, " +
                "about_me TEXT NOT NULL, " +
                "profile_pic_path VARCHAR(500), " +
                "relation_choice VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")"
            )) {
                stmt.execute();
            }
            
            // Create user_hobbies table
            try (PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS user_hobbies (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "hobby VARCHAR(100) NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")"
            )) {
                stmt.execute();
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }

    public int saveUserProfile(String username, String fullName, int height, int weight, String country, 
                             String address, String phone, String qualification, 
                             String gender, String preferences, String aboutMe, 
                             String profilePicPath, String relationChoice, 
                             List<String> hobbies) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // First, get the user_id from the users table
            int userId;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                    } else {
                        throw new SQLException("User not found: " + username);
                    }
                }
            }

            // Insert user profile with the correct user_id
            String insertUserSQL = """
                INSERT INTO user_profiles (user_id, full_name, height, age, weight, country, address, 
                                      phone, qualification, gender, preferences, 
                                      about_me, profile_pic_path, relation_choice)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, fullName);
                pstmt.setInt(3, height);
                pstmt.setNull(4, java.sql.Types.INTEGER); // TODO: Replace with actual age if available
                pstmt.setInt(5, weight);
                pstmt.setString(6, country);
                pstmt.setString(7, address);
                pstmt.setString(8, phone);
                pstmt.setString(9, qualification);
                pstmt.setString(10, gender);
                pstmt.setString(11, preferences);
                pstmt.setString(12, aboutMe);
                pstmt.setString(13, profilePicPath);
                pstmt.setString(14, relationChoice);

                pstmt.executeUpdate();
            }

            // Insert hobbies
            String insertHobbySQL = "INSERT INTO user_hobbies (user_id, hobby) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertHobbySQL)) {
                for (String hobby : hobbies) {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, hobby);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            return userId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }

    public void closeConnection() {
        DatabaseConnection.closeConnection();
    }
} 