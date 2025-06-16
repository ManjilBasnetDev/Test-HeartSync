package heartsync.controller;

import heartsync.model.Message;
import heartsync.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageController {
    private Connection conn;
    private int userId;

    public MessageController(int userId) {
        this.conn = DatabaseConnection.getConnection();
        this.userId = userId;
    }

    public void sendMessage(int receiverId, String content) {
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO messages (sender_id, receiver_id, content)
                VALUES (?, ?, ?)
            """);
            stmt.setInt(1, userId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public List<Message> getMessages(int otherUserId) {
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT * FROM messages
                WHERE (sender_id = ? AND receiver_id = ?)
                OR (sender_id = ? AND receiver_id = ?)
                ORDER BY sent_at ASC
            """);
            
            stmt.setInt(1, userId);
            stmt.setInt(2, otherUserId);
            stmt.setInt(3, otherUserId);
            stmt.setInt(4, userId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("id"),
                    rs.getInt("sender_id"),
                    rs.getInt("receiver_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_at")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get messages", e);
        }
        return messages;
    }

    public List<Integer> getChatPartners() {
        List<Integer> partners = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT DISTINCT 
                    CASE 
                        WHEN sender_id = ? THEN receiver_id
                        ELSE sender_id
                    END as partner_id
                FROM messages
                WHERE sender_id = ? OR receiver_id = ?
                ORDER BY sent_at DESC
            """);
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                partners.add(rs.getInt("partner_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get chat partners", e);
        }
        return partners;
    }
} 