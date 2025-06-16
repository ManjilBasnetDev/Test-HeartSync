package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public void sendMessage(Message message) throws SQLException {
        String sql = "INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, message.getSenderId());
            pstmt.setInt(2, message.getReceiverId());
            pstmt.setString(3, message.getMessageText());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }
        }
    }
    
    public List<Message> getMessagesBetween(int user1Id, int user2Id) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) " +
                    "OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, user1Id);
            pstmt.setInt(2, user2Id);
            pstmt.setInt(3, user2Id);
            pstmt.setInt(4, user1Id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("message_text"),
                        rs.getTimestamp("timestamp")
                    );
                    messages.add(message);
                }
            }
        }
        return messages;
    }
    
    // Optional: Method to delete messages (for cleanup or user privacy)
    public void deleteMessage(int messageId) throws SQLException {
        String sql = "DELETE FROM messages WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, messageId);
            pstmt.executeUpdate();
        }
    }
    
    // Optional: Method to get latest messages (useful for chat history loading)
    public List<Message> getLatestMessages(int user1Id, int user2Id, int limit) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) " +
                    "OR (sender_id = ? AND receiver_id = ?) " +
                    "ORDER BY timestamp DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, user1Id);
            pstmt.setInt(2, user2Id);
            pstmt.setInt(3, user2Id);
            pstmt.setInt(4, user1Id);
            pstmt.setInt(5, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("message_text"),
                        rs.getTimestamp("timestamp")
                    );
                    messages.add(0, message); // Add to beginning to maintain chronological order
                }
            }
        }
        return messages;
    }
} 