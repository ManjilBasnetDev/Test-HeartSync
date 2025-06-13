package heartsync.model;

import java.sql.Timestamp;

/**
 * Report model class represents a user report in the dating app.
 * This class follows the JavaBean pattern and provides a structured way to:
 * 1. Store report data
 * 2. Transfer data between the UI and database
 * 3. Validate data through proper getters/setters
 *
 * Fields:
 * - id: Unique identifier for the report
 * - reporterId: ID of the user making the report
 * - reportedUserId: ID of the user being reported
 * - reason: The reason for the report
 * - description: Detailed description of the issue
 * - createdAt: Timestamp of when the report was submitted
 */
public class Report {
    private int id;
    private int reporterId;
    private int reportedUserId;
    private String reason;
    private String description;
    private Timestamp createdAt;

    /**
     * Creates a new Report instance with the specified details.
     *
     * @param reporterId The ID of the user making the report
     * @param reportedUserId The ID of the user being reported
     * @param reason The reason for the report
     * @param description The detailed description of the issue
     */
    public Report(int reporterId, int reportedUserId, String reason, String description) {
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.description = description;
    }

    // Getters and setters with validation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        this.id = id;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        if (reporterId < 0) {
            throw new IllegalArgumentException("Reporter ID cannot be negative");
        }
        this.reporterId = reporterId;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        if (reportedUserId < 0) {
            throw new IllegalArgumentException("Reported user ID cannot be negative");
        }
        this.reportedUserId = reportedUserId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be empty");
        }
        this.reason = reason.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.description = description.trim();
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reporterId=" + reporterId +
                ", reportedUserId=" + reportedUserId +
                ", reason='" + reason + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 