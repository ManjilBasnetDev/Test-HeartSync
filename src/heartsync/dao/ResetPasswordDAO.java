package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResetPasswordDAO {
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordDAO.class.getName());

    public boolean resetPassword(String username, String newPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        String sql = "UPDATE users SET password = ? WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, DatabaseConnection.hashPassword(newPassword));
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Password reset successful for user: {0}", username);
                return true;
            }
            LOGGER.log(Level.WARNING, "No user found with username: {0}", username);
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error resetting password: {0}", e.getMessage());
            throw e;
        }
    }

    public boolean validateUser(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        String sql = "SELECT id FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean exists = rs.next();
                if (exists) {
                    LOGGER.log(Level.INFO, "User validated: {0}", username);
                } else {
                    LOGGER.log(Level.INFO, "User not found: {0}", username);
                }
                return exists;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating user: {0}", e.getMessage());
            throw e;
        }
    }
} 