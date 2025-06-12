package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResetDAO {
    private static final Logger LOGGER = Logger.getLogger(ResetDAO.class.getName());
    
    public boolean createUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        String sql = "INSERT INTO users (username, password, user_type, date_of_birth, email, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, DatabaseConnection.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getUserType());
            pstmt.setDate(4, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhoneNumber());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Created user: {0}", user.getUsername());
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user: {0}", e.getMessage());
            throw e;
        }
    }
    
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    LOGGER.log(Level.INFO, "Retrieved user by ID: {0}", id);
                    return user;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID: {0}", e.getMessage());
            throw e;
        }
        return null;
    }
    
    public User getUserByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    LOGGER.log(Level.INFO, "Retrieved user by username: {0}", username);
                    return user;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username: {0}", e.getMessage());
            throw e;
        }
        return null;
    }
    
    public boolean updateUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String hashedPassword = DatabaseConnection.hashPassword(user.getPassword());
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Updated user: {0}", user.getUsername());
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: {0}", e.getMessage());
            throw e;
        }
    }
    
    public boolean deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Deleted user with ID: {0}", id);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: {0}", e.getMessage());
            throw e;
        }
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            LOGGER.log(Level.INFO, "Retrieved {0} users", users.size());
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users: {0}", e.getMessage());
            throw e;
        }
        return users;
    }
    
    public boolean verifyLogin(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        String sql = "SELECT password FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error verifying login: {0}", e.getMessage());
            throw e;
        }
        return false;
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setUserType(rs.getString("user_type"));
        
        Date dobDate = rs.getDate("date_of_birth");
        if (dobDate != null) {
            user.setDateOfBirth(dobDate.toLocalDate());
        }
        
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        
        return user;
    }
} 