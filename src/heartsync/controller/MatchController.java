package heartsync.controller;

import heartsync.model.UserProfile;
import heartsync.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchController {
    private Connection conn;
    private int userId;

    public MatchController(int userId) {
        this.conn = DatabaseConnection.getConnection();
        this.userId = userId;
    }

    public List<UserProfile> getPotentialMatches() {
        List<UserProfile> matches = new ArrayList<>();
        try {
            // Get user's preferences
            PreparedStatement prefStmt = conn.prepareStatement(
                "SELECT preferences FROM user_profiles WHERE user_id = ?"
            );
            prefStmt.setInt(1, userId);
            ResultSet prefRs = prefStmt.executeQuery();
            
            if (!prefRs.next()) {
                return matches;
            }
            
            String preferences = prefRs.getString("preferences");
            
            // Find potential matches based on preferences
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT p.* FROM user_profiles p
                WHERE p.user_id != ?
                AND p.gender = ?
                AND NOT EXISTS (
                    SELECT 1 FROM matches m
                    WHERE (m.user1_id = ? AND m.user2_id = p.user_id)
                    OR (m.user1_id = p.user_id AND m.user2_id = ?)
                )
                LIMIT 10
            """);
            
            stmt.setInt(1, userId);
            stmt.setString(2, preferences);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserProfile profile = new UserProfile();
                profile.setFullName(rs.getString("full_name"));
                profile.setHeight(rs.getInt("height"));
                profile.setWeight(rs.getInt("weight"));
                profile.setAge(rs.getInt("age"));
                profile.setCountry(rs.getString("country"));
                profile.setAddress(rs.getString("address"));
                profile.setPhoneNumber(rs.getString("phone_number"));
                profile.setQualification(rs.getString("qualification"));
                profile.setGender(rs.getString("gender"));
                profile.setPreferences(rs.getString("preferences"));
                profile.setAboutMe(rs.getString("about_me"));
                profile.setProfilePicPath(rs.getString("profile_pic_path"));
                matches.add(profile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get potential matches", e);
        }
        return matches;
    }

    public void likeProfile(int likedUserId) {
        try {
            // Check if the other user has already liked this user
            PreparedStatement checkStmt = conn.prepareStatement("""
                SELECT id FROM matches
                WHERE user1_id = ? AND user2_id = ?
                AND status = 'liked'
            """);
            checkStmt.setInt(1, likedUserId);
            checkStmt.setInt(2, userId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // It's a match! Update the status
                PreparedStatement updateStmt = conn.prepareStatement("""
                    UPDATE matches
                    SET status = 'matched'
                    WHERE user1_id = ? AND user2_id = ?
                """);
                updateStmt.setInt(1, likedUserId);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
            } else {
                // Create a new like
                PreparedStatement insertStmt = conn.prepareStatement("""
                    INSERT INTO matches (user1_id, user2_id, status)
                    VALUES (?, ?, 'liked')
                """);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, likedUserId);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to like profile", e);
        }
    }

    public void dislikeProfile(int dislikedUserId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO matches (user1_id, user2_id, status)
                VALUES (?, ?, 'disliked')
            """);
            stmt.setInt(1, userId);
            stmt.setInt(2, dislikedUserId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to dislike profile", e);
        }
    }

    public List<UserProfile> getMatches() {
        List<UserProfile> matches = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT p.* FROM user_profiles p
                JOIN matches m ON (m.user1_id = p.user_id OR m.user2_id = p.user_id)
                WHERE (m.user1_id = ? OR m.user2_id = ?)
                AND m.status = 'matched'
                AND p.user_id != ?
            """);
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserProfile profile = new UserProfile();
                profile.setFullName(rs.getString("full_name"));
                profile.setHeight(rs.getInt("height"));
                profile.setWeight(rs.getInt("weight"));
                profile.setAge(rs.getInt("age"));
                profile.setCountry(rs.getString("country"));
                profile.setAddress(rs.getString("address"));
                profile.setPhoneNumber(rs.getString("phone_number"));
                profile.setQualification(rs.getString("qualification"));
                profile.setGender(rs.getString("gender"));
                profile.setPreferences(rs.getString("preferences"));
                profile.setAboutMe(rs.getString("about_me"));
                profile.setProfilePicPath(rs.getString("profile_pic_path"));
                matches.add(profile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get matches", e);
        }
        return matches;
    }
} 