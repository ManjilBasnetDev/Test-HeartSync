package heartsync.dao;

import heartsync.database.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteUserDAO {
    
    /**
     * Deletes a user and all associated data from the database
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUserAndAssociatedData(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Delete user's health data
            deleteHealthData(conn, userId);
            
            // Delete user's profile
            deleteUserProfile(conn, userId);
            
            // Delete user's reports (both as reporter and reported)
            deleteUserReports(conn, userId);
            
            // Finally delete the user
            boolean userDeleted = deleteUser(conn, userId);
            
            if (userDeleted) {
                conn.commit(); // Commit transaction if all operations successful
                return true;
            } else {
                conn.rollback(); // Rollback if user deletion failed
                return false;
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on any error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void deleteHealthData(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM health_data WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    private void deleteUserProfile(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM user_profiles WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    private void deleteUserReports(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM reports WHERE reporter_id = ? OR reported_user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }
    
    private boolean deleteUser(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
} 