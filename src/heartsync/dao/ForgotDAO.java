package heartsync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import heartsyncdatingapp.database.DatabaseConnection;
import heartsyncdatingapp.model.UserForgot;

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
        String query = "SELECT * FROM users WHERE username = ? AND favorite_color = ? AND first_school = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, favoriteColor);
            stmt.setString(3, firstSchool);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    public void updatePassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            
            stmt.executeUpdate();
        }
    }
} 