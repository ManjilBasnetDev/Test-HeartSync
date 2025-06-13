package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.Report;
import java.sql.*;

/**
 * Data Access Object for handling Report-related database operations.
 * Implements CRUD operations for Report entities.
 */
public class ReportDAO {
    private final DatabaseConnection dbConnection;
    
    public ReportDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        createReportTable();
    }
    
    /**
     * Creates the reports table if it doesn't exist.
     */
    private void createReportTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS reports (
                id INT PRIMARY KEY AUTO_INCREMENT,
                reporter_id INT NOT NULL,
                reported_user_id INT NOT NULL,
                reason VARCHAR(100) NOT NULL,
                description TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (reporter_id) REFERENCES users(id),
                FOREIGN KEY (reported_user_id) REFERENCES users(id)
            )
        """;
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating reports table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a new report in the database.
     * @param report The report to create
     * @return true if the report was created successfully, false otherwise
     */
    public boolean createReport(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Report cannot be null");
        }

        String sql = "INSERT INTO reports (reporter_id, reported_user_id, reason, description) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, report.getReporterId());
            pstmt.setInt(2, report.getReportedUserId());
            pstmt.setString(3, report.getReason());
            pstmt.setString(4, report.getDescription());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        report.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error creating report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves a report by its ID.
     * @param id The ID of the report to retrieve
     * @return The report if found, null otherwise
     */
    public Report getReportById(int id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractReportFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving report: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retrieves all reports made by a specific user.
     * @param reporterId The ID of the user who made the reports
     * @return A list of reports made by the user
     */
    public java.util.List<Report> getReportsByReporterId(int reporterId) {
        java.util.List<Report> reports = new java.util.ArrayList<>();
        String sql = "SELECT * FROM reports WHERE reporter_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reporterId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving reports: " + e.getMessage());
            e.printStackTrace();
        }
        return reports;
    }
    
    /**
     * Retrieves all reports made against a specific user.
     * @param reportedUserId The ID of the user who was reported
     * @return A list of reports made against the user
     */
    public java.util.List<Report> getReportsByReportedUserId(int reportedUserId) {
        java.util.List<Report> reports = new java.util.ArrayList<>();
        String sql = "SELECT * FROM reports WHERE reported_user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reportedUserId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving reports: " + e.getMessage());
            e.printStackTrace();
        }
        return reports;
    }
    
    /**
     * Extracts a Report object from a ResultSet.
     * @param rs The ResultSet containing the report data
     * @return A Report object with the data from the ResultSet
     * @throws SQLException if there's an error accessing the ResultSet
     */
    private Report extractReportFromResultSet(ResultSet rs) throws SQLException {
        Report report = new Report(
            rs.getInt("reporter_id"),
            rs.getInt("reported_user_id"),
            rs.getString("reason"),
            rs.getString("description")
        );
        report.setId(rs.getInt("id"));
        report.setCreatedAt(rs.getTimestamp("created_at"));
        return report;
    }
} 