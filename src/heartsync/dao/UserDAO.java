package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    
    public boolean authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        String sql = "SELECT password FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String inputHash = DatabaseConnection.hashPassword(password);
                    boolean matches = storedHash.equals(inputHash);
                    
                    if (matches) {
                        LOGGER.log(Level.INFO, "Login successful for username: {0}", username);
                    } else {
                        LOGGER.log(Level.INFO, "Login failed for username: {0} - Invalid password", username);
                    }
                    
                    return matches;
                } else {
                    LOGGER.log(Level.INFO, "Login failed - Username not found: {0}", username);
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during authentication", e);
            throw new SQLException("Authentication failed: " + e.getMessage(), e);
        }
    }
    
    public int createUser(User user) throws SQLException {
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
        
        String sql = "INSERT INTO users (username, password, user_type, email, phone_number, " +
                    "date_of_birth, gender, interests, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, DatabaseConnection.hashPassword(user.getPassword()));
            stmt.setString(3, user.getUserType());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setDate(6, user.getDateOfBirth() != null ? 
                           Date.valueOf(user.getDateOfBirth()) : null);
            stmt.setString(7, user.getGender());
            stmt.setString(8, user.getInterests());
            stmt.setString(9, user.getBio());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    user.setId(id);
                    LOGGER.log(Level.INFO, "Created new user with ID: {0}", id);
                    return id;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user: {0}", e.getMessage());
            throw new SQLException("Failed to create user: " + e.getMessage(), e);
        }
    }
    
    public User getUser(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    LOGGER.log(Level.INFO, "Retrieved user: {0}", username);
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user: {0}", e.getMessage());
            throw new SQLException("Failed to retrieve user: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
            LOGGER.log(Level.INFO, "Retrieved {0} users", users.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving users: {0}", e.getMessage());
            throw new SQLException("Failed to retrieve users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    public boolean updateUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        String sql = "UPDATE users SET password = ?, email = ?, phone_number = ?, " +
                    "date_of_birth = ?, gender = ?, interests = ?, bio = ? WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, DatabaseConnection.hashPassword(user.getPassword()));
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setDate(4, user.getDateOfBirth() != null ? 
                           Date.valueOf(user.getDateOfBirth()) : null);
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getInterests());
            stmt.setString(7, user.getBio());
            stmt.setString(8, user.getUsername());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Updated user: {0}", user.getUsername());
            } else {
                LOGGER.log(Level.WARNING, "No user found to update: {0}", user.getUsername());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: {0}", e.getMessage());
            throw new SQLException("Failed to update user: " + e.getMessage(), e);
        }
    }
    
    public boolean deleteUser(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Deleted user: {0}", username);
            } else {
                LOGGER.log(Level.WARNING, "No user found to delete: {0}", username);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: {0}", e.getMessage());
            throw new SQLException("Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // Note: This is the hashed password
        user.setUserType(rs.getString("user_type"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        
        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth.toLocalDate());
        }
        
        user.setGender(rs.getString("gender"));
        user.setInterests(rs.getString("interests"));
        user.setBio(rs.getString("bio"));
        
        return user;
    }
} 