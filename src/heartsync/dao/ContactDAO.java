package heartsync.dao;

import heartsync.database.DatabaseConnection;
import heartsync.model.Contact;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for handling Contact-related database operations.
 * Implements CRUD operations for Contact entities.
 */
public class ContactDAO {
    private static final Logger LOGGER = Logger.getLogger(ContactDAO.class.getName());
    private final Connection connection;
    
    public ContactDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        if (this.connection == null) {
            throw new SQLException("Could not establish database connection");
        }
        createContactTable();
    }
    
    /**
     * Creates the contacts table if it doesn't exist.
     * @throws SQLException if there's an error executing the SQL
     */
    private void createContactTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS contacts (
                id INT PRIMARY KEY AUTO_INCREMENT,
                full_name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL,
                message TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Contacts table created or verified");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating contacts table", e);
            throw e;
        }
    }
    
    /**
     * Saves a new contact to the database.
     * @param contact The contact to save
     * @return true if the save was successful, false otherwise
     * @throws SQLException if there's an error executing the SQL
     * @throws IllegalArgumentException if contact is null or contains invalid data
     */
    public boolean saveContact(Contact contact) throws SQLException {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        String sql = "INSERT INTO contacts (full_name, email, message) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, contact.getFullName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getMessage());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contact.setId(generatedKeys.getInt(1));
                        contact.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                        LOGGER.info("Contact saved successfully with ID: " + contact.getId());
                        return true;
                    }
                }
            }
            
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving contact", e);
            throw e;
        }
    }
    
    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database connection", e);
        }
    }
}