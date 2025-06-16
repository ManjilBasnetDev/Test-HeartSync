package heartsync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import heartsync.database.DatabaseConnection;
import heartsync.model.UserForgot;

/**
 * DAO dedicated to Forgot-Password flow.
 * Moved into its own file so the public class name matches the filename.
 */
public class UserDAOForgot {
    public UserForgot findByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserForgot user = new UserForgot();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFavoriteColor(rs.getString("favorite_color"));
                user.setFirstSchool(rs.getString("first_school"));
                return user;
            }
        }
        return null;
    }

    public boolean validateSecurityQuestions(String username, String favoriteColor, String firstSchool) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND LOWER(favorite_color) = LOWER(?) AND LOWER(first_school) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, favoriteColor.trim());
            stmt.setString(3, firstSchool.trim());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error validating security questions: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean saveSecurityQuestions(String username, String favoriteColor, String firstSchool) throws SQLException {
        String sql = "UPDATE users SET favorite_color = ?, first_school = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, favoriteColor.trim());
            stmt.setString(2, firstSchool.trim());
            stmt.setString(3, username);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updatePassword(String username, String newPlainPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new SQLException("Username cannot be null or empty");
        }
        if (newPlainPassword == null || newPlainPassword.trim().isEmpty()) {
            throw new SQLException("New password cannot be null or empty");
        }

        String query = "UPDATE users SET password = ? WHERE username = ?";
        System.out.println("Executing query: " + query);
        System.out.println("Username: " + username);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to get database connection");
            }
            
            // Disable auto-commit so we can manually commit the transaction
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                // Hash the password before storing
                String hashedPassword = DatabaseConnection.hashPassword(newPlainPassword);
                System.out.println("Hashed password: " + hashedPassword);
                
                stmt.setString(1, hashedPassword);
                stmt.setString(2, username);
                
                int rowsUpdated = stmt.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
                
                if (rowsUpdated == 0) {
                    throw new SQLException("No user found with username: " + username);
                }
                
                // Commit the transaction manually
                conn.commit();
                return rowsUpdated > 0;
            } finally {
                // Always restore the original auto-commit state
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {}
            }
        } catch (SQLException e) {
            System.err.println("SQL Error updating password for user: " + username);
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error updating password for user: " + username);
            e.printStackTrace();
            throw new SQLException("Failed to update password: " + e.getMessage(), e);
        }
    }
}
