package heartsync.dao;

import heartsync.model.User;
import heartsync.database.MySqlConnection;
import heartsync.database.DatabaseConnection;
import java.sql.*;

public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        try {
            this.connection = MySqlConnection.getConnection();
            createDefaultUsers(); // Create default users if they don't exist
        } catch (SQLException e) {
            System.err.println("Failed to initialize UserDAO: " + e.getMessage());
            this.connection = null;
        }
    }
    
    // Create default admin and user accounts if they don't exist
    private void createDefaultUsers() {
        if (connection == null) return;
        
        try {
            // Ensure user_type column exists in the users table
            try (Statement stmt = connection.createStatement()) {
                // Check if the column exists
                ResultSet rs = connection.getMetaData().getColumns(null, null, "users", "user_type");
                if (!rs.next()) {
                    // Column doesn't exist, add it
                    String alterTableSQL = "ALTER TABLE users ADD COLUMN user_type VARCHAR(20) DEFAULT 'user';";
                    stmt.execute(alterTableSQL);
                    System.out.println("Added user_type column to users table");
                }
            } catch (SQLException e) {
                System.err.println("Error ensuring user_type column exists: " + e.getMessage());
            }
            // Create default admin user if it doesn't exist
            if (!usernameExists("Admin")) {
                User admin = new User();
                admin.setUsername("Admin");
                admin.setPassword("Admin@123");
                admin.setEmail("admin@heartsync.com");
                admin.setUserType("ADMIN");
                createUser(admin);
                System.out.println("Created default admin user");
            }
            
            // Create default regular user if it doesn't exist
            if (!usernameExists("User")) {
                User regularUser = new User();
                regularUser.setUsername("User");
                regularUser.setUserType("USER");
                regularUser.setPassword("User@123");
                regularUser.setEmail("user@heartsync.com");
                createUser(regularUser);
                System.out.println("Created default regular user");
            }
        } catch (Exception e) {
            System.err.println("Error creating default users: " + e.getMessage());
        }
    }
    
    // Helper to safely get column if it exists
    private String getStringIfExists(java.sql.ResultSet rs, String column) throws java.sql.SQLException {
        try {
            rs.findColumn(column);
            return rs.getString(column);
        } catch (java.sql.SQLException ex) {
            // Column not present
            return null;
        }
    }

    // Authenticate user login
    public User authenticateUser(String username, String password) {
        System.out.println("Authenticating user: " + username);
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Get the stored hashed password
                String storedHashedPassword = rs.getString("password");
                
                // Hash the input password for comparison
                String hashedInputPassword = DatabaseConnection.hashPassword(password);
                
                // Debug output for password comparison
                System.out.println("Stored hash: " + storedHashedPassword);
                System.out.println("Input hash:  " + hashedInputPassword);
                System.out.println("Match: " + storedHashedPassword.equals(hashedInputPassword));
                
                // Compare the hashed passwords
                if (storedHashedPassword.equals(hashedInputPassword)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(storedHashedPassword);
                    user.setEmail(rs.getString("email"));
                    user.setUserType(rs.getString("user_type"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // Authentication failed
    }
    
    // Create new user
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, user_type) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            // Hash the password before storing
            String hashedPassword = DatabaseConnection.hashPassword(user.getPassword());
            System.out.println("Creating user " + user.getUsername() + " with hashed password: " + hashedPassword);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserType() != null ? user.getUserType().toUpperCase() : "USER");
            
            int rowsAffected = pstmt.executeUpdate();
            
            // If this is one of the default users, update the password in the user object to match what's stored
            if (user.getUsername().equals("Admin") || user.getUsername().equals("User")) {
                user.setPassword(hashedPassword);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if username exists
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        
        return false;
    }
    
    // Get user by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setUserType(rs.getString("user_type"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update user password
    public boolean updatePassword(String username, String newPassword) {
        // Hash the password before updating to maintain consistency
        String hashedPassword = DatabaseConnection.hashPassword(newPassword);
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
}
