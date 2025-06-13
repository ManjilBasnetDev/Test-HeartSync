/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;

public class UserDAOLogin {
    private static final Logger LOGGER = Logger.getLogger(UserDAOLogin.class.getName());
    private final DatabaseConnection dbConnection;
    
    public UserDAOLogin() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, DatabaseConnection.hashPassword(password));
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during authentication", e);
            throw new SQLException("Authentication failed: " + e.getMessage(), e);
        }
    }
    
    public User getUserByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user", e);
            throw new SQLException("Failed to retrieve user: " + e.getMessage(), e);
        }
        return null;
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setUserType(rs.getString("user_type"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setDateOfBirth(rs.getString("date_of_birth"));
        user.setGender(rs.getString("gender"));
        user.setInterests(rs.getString("interests"));
        user.setBio(rs.getString("bio"));
        return user;
    }
    
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        
        // Validate password requirements
        DatabaseConnection.validatePassword(newPassword);
        
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, DatabaseConnection.hashPassword(newPassword));
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                LOGGER.warning("No user found with username: " + username);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password", e);
            throw new SQLException("Failed to update password: " + e.getMessage(), e);
        }
    }
    
    public boolean createUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        // Validate user data
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Validate password requirements
        DatabaseConnection.validatePassword(user.getPassword());
        
        if (user.getEmail() != null) {
            DatabaseConnection.validateEmail(user.getEmail());
        }
        
        String sql = "INSERT INTO users (username, password, user_type, email, phone_number, date_of_birth, gender, interests, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, DatabaseConnection.hashPassword(user.getPassword()));
            stmt.setString(3, user.getUserType());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getDateOfBirth());
            stmt.setString(7, user.getGender());
            stmt.setString(8, user.getInterests());
            stmt.setString(9, user.getBio());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            throw new SQLException("Failed to create user: " + e.getMessage(), e);
        }
    }
    
    public void closeConnection() {
        dbConnection.closeConnection();
    }
} 