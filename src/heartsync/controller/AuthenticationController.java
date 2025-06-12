package heartsync.controller;

import heartsync.dao.UserDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationController {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class.getName());
    private final UserDAO userDAO;
    
    public static class ValidationResult {
        private final boolean success;
        private final String message;
        
        public ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    public AuthenticationController() {
        this.userDAO = new UserDAO();
    }
    
    public ValidationResult validateLogin(String username, String password) {
        // Validate username
        if (username == null || username.trim().isEmpty() || username.equals("USERNAME")) {
            return new ValidationResult(false, "Please enter a username");
        }
        
        if (username.length() < 3) {
            return new ValidationResult(false, "Username must be at least 3 characters long");
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return new ValidationResult(false, "Username can only contain letters, numbers, and underscores");
        }
        
        // Validate password
        if (password == null || password.trim().isEmpty() || password.equals("Enter password")) {
            return new ValidationResult(false, "Please enter a password");
        }
        
        if (password.length() < 6) {
            return new ValidationResult(false, "Password must be at least 6 characters long");
        }
        
        return new ValidationResult(true, "Validation successful");
    }
    
    public boolean authenticate(String username, String password) throws SQLException {
        try {
            ValidationResult validation = validateLogin(username, password);
            if (!validation.isSuccess()) {
                LOGGER.log(Level.WARNING, "Login validation failed: {0}", validation.getMessage());
                throw new IllegalArgumentException(validation.getMessage());
            }
            
            boolean authenticated = userDAO.authenticate(username, password);
            if (authenticated) {
                LOGGER.log(Level.INFO, "User successfully authenticated: {0}", username);
            } else {
                LOGGER.log(Level.INFO, "Authentication failed for user: {0}", username);
            }
            return authenticated;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during authentication", e);
            throw e;
        }
    }
}
