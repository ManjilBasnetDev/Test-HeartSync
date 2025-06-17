package heartsync.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import heartsync.database.DatabaseConnection;
import heartsync.model.User;

public class LikeDAO {
    public boolean saveLike(int likerId, int likedId) {
        String sql = "INSERT INTO likes (liker_user_id, liked_user_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE liker_user_id=liker_user_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, likerId);
            pstmt.setInt(2, likedId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean likeExists(int likerId, int likedId) {
        String sql = "SELECT 1 FROM likes WHERE liker_user_id = ? AND liked_user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, likerId);
            pstmt.setInt(2, likedId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getLikesByUser(int userId) {
        List<User> likes = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN likes l ON u.id = l.liked_user_id WHERE l.liker_user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setUserType(rs.getString("user_type"));
                likes.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    public List<User> getLikersOfUser(int userId) {
        List<User> likers = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN likes l ON u.id = l.liker_user_id WHERE l.liked_user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setUserType(rs.getString("user_type"));
                likers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likers;
    }
} 