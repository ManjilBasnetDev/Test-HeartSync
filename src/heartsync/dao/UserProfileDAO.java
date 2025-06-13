package heartsync.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import heartsync.database.MySqlConnection;
import heartsync.model.UserProfile;

public class UserProfileDAO {
    private static final Logger LOGGER = Logger.getLogger(UserProfileDAO.class.getName());
    private final MySqlConnection dbConnection;

    public UserProfileDAO() {
        this.dbConnection = new MySqlConnection();
    }

    public int createUserProfile(UserProfile userProfile) throws SQLException {
        String sql = "INSERT INTO user_profiles (user_id, email, phone_number, date_of_birth, gender, interests, bio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userProfile.getUserId());
            pstmt.setString(2, userProfile.getEmail());
            pstmt.setString(3, userProfile.getPhoneNumber());
            pstmt.setDate(4, Date.valueOf(userProfile.getDateOfBirth()));
            pstmt.setString(5, userProfile.getGender());
            pstmt.setString(6, userProfile.getInterests());
            pstmt.setString(7, userProfile.getBio());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user profile failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user profile failed, no ID obtained.");
                }
            }
        }
    }

    public UserProfile getUserProfileById(int id) throws SQLException {
        String sql = "SELECT * FROM user_profiles WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserProfile(rs);
                }
            }
        }
        return null;
    }

    public UserProfile getUserProfileByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM user_profiles WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserProfile(rs);
                }
            }
        }
        return null;
    }

    public List<UserProfile> getAllUserProfiles() throws SQLException {
        String sql = "SELECT * FROM user_profiles";
        List<UserProfile> profiles = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                profiles.add(mapResultSetToUserProfile(rs));
            }
        }
        return profiles;
    }

    public boolean updateUserProfile(UserProfile userProfile) throws SQLException {
        String sql = "UPDATE user_profiles SET email = ?, phone_number = ?, date_of_birth = ?, gender = ?, interests = ?, bio = ? WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userProfile.getEmail());
            pstmt.setString(2, userProfile.getPhoneNumber());
            pstmt.setDate(3, Date.valueOf(userProfile.getDateOfBirth()));
            pstmt.setString(4, userProfile.getGender());
            pstmt.setString(5, userProfile.getInterests());
            pstmt.setString(6, userProfile.getBio());
            pstmt.setInt(7, userProfile.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteUserProfile(int id) throws SQLException {
        String sql = "DELETE FROM user_profiles WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private UserProfile mapResultSetToUserProfile(ResultSet rs) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setId(rs.getInt("id"));
        profile.setUserId(rs.getInt("user_id"));
        profile.setEmail(rs.getString("email"));
        profile.setPhoneNumber(rs.getString("phone_number"));
        profile.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        profile.setGender(rs.getString("gender"));
        profile.setInterests(rs.getString("interests"));
        profile.setBio(rs.getString("bio"));
        return profile;
    }
} 