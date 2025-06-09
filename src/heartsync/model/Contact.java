package heartsync.model;

import java.sql.Timestamp;

/**
 * Contact model class represents a contact form submission in the dating app.
 * This class follows the JavaBean pattern and provides a structured way to:
 * 1. Store contact form data
 * 2. Transfer data between the UI and database
 * 3. Validate data through proper getters/setters
 *
 * Fields:
 * - id: Unique identifier for the contact submission
 * - fullName: Name of the person submitting the contact form
 * - email: Email address for contact
 * - message: The actual message content
 * - createdAt: Timestamp of when the contact was submitted
 */
public class Contact {
    private int id;
    private String fullName;
    private String email;
    private String message;
    private Timestamp createdAt;

    /**
     * Creates a new Contact instance with the specified details.
     *
     * @param fullName The full name of the person submitting the contact form
     * @param email The email address for contact
     * @param message The message content
     */
    public Contact(String fullName, String email, String message) {
        this.fullName = fullName;
        this.email = email;
        this.message = message;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        this.message = message.trim();
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 