package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgotDAO {
    private static final Logger LOGGER = Logger.getLogger(ForgotDAO.class.getName());
    
    public User findByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    LOGGER.log(Level.INFO, "Found user: {0}", username);
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username: {0}", e.getMessage());
            throw e;
        }
        return null;
    }
    
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
            
            stmt.setString(1, DatabaseConnection.hashPassword(newPassword));
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Updated password for user: {0}", username);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", e.getMessage());
            throw e;
        }
    }
    
    public boolean validateSecurityAnswers(String username, String favoriteColor, String firstSchool) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM users WHERE username = ? AND favorite_color = ? AND first_school = ?")) {
            
            stmt.setString(1, username);
            stmt.setString(2, favoriteColor);
            stmt.setString(3, firstSchool);
            
            try (ResultSet rs = stmt.executeQuery()) {
                boolean isValid = rs.next();
                if (isValid) {
                    LOGGER.log(Level.INFO, "Security answers validated for user: {0}", username);
                } else {
                    LOGGER.log(Level.INFO, "Invalid security answers for user: {0}", username);
                }
                return isValid;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating security answers: {0}", e.getMessage());
            throw e;
        }
    }
} 