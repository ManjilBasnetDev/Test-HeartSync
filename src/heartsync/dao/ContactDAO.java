package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.Contact;
import java.sql.*;

/**
 * Data Access Object for handling Contact-related database operations.
 * Implements CRUD operations for Contact entities.
 */
public class ContactDAO {
    
    public ContactDAO() {
        // Table creation is now handled by DatabaseConnection initialization
    }
    
    /**
     * Saves a new contact to the database.
     * @param contact The contact to save
     * @return true if the save was successful, false otherwise
     * @throws SQLException if there's an error executing the SQL
     */
    public boolean saveContact(Contact contact) throws SQLException {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        String sql = "INSERT INTO contacts (full_name, email, message) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, contact.getFullName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getMessage());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contact.setId(generatedKeys.getInt(1));
                        System.out.println("Contact saved successfully with ID: " + contact.getId());
                        return true;
                    }
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error saving contact: " + e.getMessage());
            throw e;
        }
    }
} 