package heartsync.dao;

import heartsync.database.DatabaseConfig;
import heartsync.model.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    
    public boolean createReport(Report report) {
        String sql = "INSERT INTO reports (reporter_id, reported_user_id, reason, description, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, report.getReporterId());
            pstmt.setInt(2, report.getReportedUserId());
            pstmt.setString(3, report.getReason());
            pstmt.setString(4, report.getDescription());
            pstmt.setString(5, report.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        report.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Report getReportById(int id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractReportFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Report> getReportsByReportedUser(int reportedUserId) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE reported_user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reportedUserId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    
    public boolean updateReportStatus(int reportId, String newStatus) {
        String sql = "UPDATE reports SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, reportId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Report extractReportFromResultSet(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setId(rs.getInt("id"));
        report.setReporterId(rs.getInt("reporter_id"));
        report.setReportedUserId(rs.getInt("reported_user_id"));
        report.setReason(rs.getString("reason"));
        report.setDescription(rs.getString("description"));
        report.setStatus(rs.getString("status"));
        report.setCreatedAt(rs.getTimestamp("created_at"));
        return report;
    }
} 