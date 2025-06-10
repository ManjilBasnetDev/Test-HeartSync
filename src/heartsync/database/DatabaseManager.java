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
            try (PreparedStatement stmt = conn.prepareStatement(DatabaseConfig.CREATE_USERS_TABLE)) {
                stmt.execute();
            }
            
            // Create hobbies table
            try (PreparedStatement stmt = conn.prepareStatement(DatabaseConfig.CREATE_HOBBIES_TABLE)) {
                stmt.execute();
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }

    public int saveUserProfile(String fullName, int height, int weight, String country, 
                             String address, String phone, String qualification, 
                             String gender, String preferences, String aboutMe, 
                             String profilePicPath, String relationChoice, 
                             List<String> hobbies) throws SQLException {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert user profile
            String insertUserSQL = """
                INSERT INTO users (full_name, height, weight, country, address, 
                                 phone, qualification, gender, preferences, 
                                 about_me, profile_pic_path, relation_choice)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            int userId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, fullName);
                pstmt.setInt(2, height);
                pstmt.setInt(3, weight);
                pstmt.setString(4, country);
                pstmt.setString(5, address);
                pstmt.setString(6, phone);
                pstmt.setString(7, qualification);
                pstmt.setString(8, gender);
                pstmt.setString(9, preferences);
                pstmt.setString(10, aboutMe);
                pstmt.setString(11, profilePicPath);
                pstmt.setString(12, relationChoice);

                pstmt.executeUpdate();

                // Get the generated user ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get user ID");
                    }
                }
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
        dbConnection.closeConnection();
    }
} 