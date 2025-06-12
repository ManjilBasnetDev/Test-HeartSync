package heartsync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import heartsync.database.DatabaseConnection;
import heartsyncdatingapp.model.User;

public class LikeDAO {
    private Connection connection;

    public LikeDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addLike(int likerId, int likedUserId) {
        String sql = "INSERT INTO likes (liker_id, liked_user_id, timestamp) VALUES (?, ?, NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, likerId);
            pstmt.setInt(2, likedUserId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeLike(int likerId, int likedUserId) {
        String sql = "DELETE FROM likes WHERE liker_id = ? AND liked_user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, likerId);
            pstmt.setInt(2, likedUserId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getLikers(int userId) {
        List<User> likers = new ArrayList<>();
        
        // Add dummy users for testing
        User rajeshDai = new User();
        rajeshDai.setId(101);
        rajeshDai.setUsername("Rajesh Dai");
        rajeshDai.setBio("Movie star from Nepal");
        rajeshDai.setInterests("Acting, Movies, Dancing");
        likers.add(rajeshDai);

        User user2 = new User();
        user2.setId(102);
        user2.setUsername("Karishma Dai");
        user2.setBio("Actress and Model");
        user2.setInterests("Fashion, Dancing, Movies");
        likers.add(user2);

        User user3 = new User();
        user3.setId(103);
        user3.setUsername("Bhuwan KC");
        user3.setBio("Veteran Actor and Director");
        user3.setInterests("Film Direction, Acting");
        likers.add(user3);

        return likers;
    }

    public boolean hasLiked(int likerId, int likedUserId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE liker_id = ? AND liked_user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, likerId);
            pstmt.setInt(2, likedUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
