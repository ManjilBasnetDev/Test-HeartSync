package heartsync.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;

public class ResetDAO {
    
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, user_type, phone_number, date_of_birth, gender, interests, bio, favorite_color, first_school) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, DatabaseConnection.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserType() != null ? user.getUserType() : "user");
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setString(6, user.getDateOfBirth());
            pstmt.setString(7, user.getGender());
            pstmt.setString(8, user.getInterests());
            pstmt.setString(9, user.getBio());
            pstmt.setString(10, user.getFavoriteColor());
            pstmt.setString(11, user.getFirstSchool());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone_number = ?, date_of_birth = ?, " +
                   "gender = ?, interests = ?, bio = ?, favorite_color = ?, first_school = ? " +
                   "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getDateOfBirth());
            pstmt.setString(5, user.getGender());
            pstmt.setString(6, user.getInterests());
            pstmt.setString(7, user.getBio());
            pstmt.setString(8, user.getFavoriteColor());
            pstmt.setString(9, user.getFirstSchool());
            pstmt.setInt(10, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean verifyLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next(); // Returns true if user exists with given credentials
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setUserType(rs.getString("user_type"));
        user.setPhoneNumber(rs.getString("phone_number"));
        
        // Handle date of birth
        String dob = rs.getString("date_of_birth");
        if (dob != null) {
            user.setDateOfBirth(dob);
        }
        
        user.setGender(rs.getString("gender"));
        user.setInterests(rs.getString("interests"));
        user.setBio(rs.getString("bio"));
        user.setFavoriteColor(rs.getString("favorite_color"));
        user.setFirstSchool(rs.getString("first_school"));
        
        // Handle created_at if it exists in the result set
        try {
            Date createdAt = rs.getDate("created_at");
            if (createdAt != null) {
                user.setCreatedAt(createdAt);
            }
        } catch (SQLException e) {
            // created_at column might not exist in all queries
        }
        
        return user;
    }
} 