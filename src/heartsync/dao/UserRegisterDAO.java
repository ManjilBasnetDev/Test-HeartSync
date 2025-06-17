package heartsync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import heartsync.database.DatabaseConnection;
import heartsync.model.User;

public class UserRegisterDAO {
    private Connection connection;
    
    public UserRegisterDAO() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }
    
    public boolean createUser(User user) throws SQLException {
        // First check if username already exists
        if (usernameExists(user.getUsername(), connection)) {
            throw new SQLException("Username already exists");
        }

        // Calculate age from dob
        int age = 0;
        String dob = user.getDateOfBirth();
        if (dob != null && !dob.isEmpty()) {
            age = DatabaseConnection.calculateAge(java.time.LocalDate.parse(dob));
        }

        // Insert into users (NO date_of_birth field)
        boolean hasSecurityQuestions = user.getFavoriteColor() != null && user.getFirstSchool() != null;
        String sql = "INSERT INTO users (username, password, user_type, first_school, favorite_color, age, date_of_birth) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int paramIndex = 1;
            statement.setString(paramIndex++, user.getUsername());
            statement.setString(paramIndex++, DatabaseConnection.hashPassword(user.getPassword()));
            statement.setString(paramIndex++, user.getUserType() != null ? user.getUserType() : "USER");
            statement.setString(paramIndex++, user.getFirstSchool());
            statement.setString(paramIndex++, user.getFavoriteColor());
            statement.setInt(paramIndex++, age);
            statement.setString(paramIndex++, dob);
            
            int rowsInserted = statement.executeUpdate();
            
            if (rowsInserted > 0) {
                // Get the generated user ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        // Do NOT create user profile here. It will be created after profile setup.
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    private void createUserProfile(User user, String dob, int age) throws SQLException {
        String sql = "INSERT INTO user_profiles (user_id, full_name, date_of_birth, age) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, dob);
            stmt.setInt(4, age);
            stmt.executeUpdate();
        }
    }
    
    private void updateUserAdditionalInfo(User user) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        boolean needsComma = false;
        
        if (user.getEmail() != null) {
            sql.append("email = ?");
            needsComma = true;
        }
        
        if (user.getPhoneNumber() != null) {
            if (needsComma) sql.append(", ");
            sql.append("phone_number = ?");
            needsComma = true;
        }
        
        if (user.getDateOfBirth() != null) {
            if (needsComma) sql.append(", ");
            sql.append("date_of_birth = ?");
            needsComma = true;
        }
        
        // Add security questions if provided
        if (user.getFavoriteColor() != null) {
            if (needsComma) sql.append(", ");
            sql.append("favorite_color = ?");
            needsComma = true;
        }
        
        if (user.getFirstSchool() != null) {
            if (needsComma) sql.append(", ");
            sql.append("first_school = ?");
            needsComma = true;
        }
        
        sql.append(" WHERE id = ?");
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (user.getEmail() != null) {
                stmt.setString(paramIndex++, user.getEmail());
            }
            
            if (user.getPhoneNumber() != null) {
                stmt.setString(paramIndex++, user.getPhoneNumber());
            }
            
            if (user.getDateOfBirth() != null) {
                stmt.setString(paramIndex++, user.getDateOfBirth());
            }
            
            // Set security questions if provided
            if (user.getFavoriteColor() != null) {
                stmt.setString(paramIndex++, user.getFavoriteColor());
            }
            
            if (user.getFirstSchool() != null) {
                stmt.setString(paramIndex++, user.getFirstSchool());
            }
            
            stmt.setInt(paramIndex, user.getId());
            stmt.executeUpdate();
        }
    }
    
    private boolean usernameExists(String username, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }
    
    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next(); // Returns true if user exists with given credentials
        } catch (SQLException e) {
            System.out.println("Error validating user: " + e.getMessage());
            return false;
        }
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
        return users;
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, phone_number = ?, date_of_birth = ?, " +
                   "gender = ?, interests = ?, bio = ?, favorite_color = ?, first_school = ? " +
                   "WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPhoneNumber());
            stmt.setString(3, user.getDateOfBirth());
            stmt.setString(4, user.getGender());
            stmt.setString(5, user.getInterests());
            stmt.setString(6, user.getBio());
            stmt.setString(7, user.getFavoriteColor());
            stmt.setString(8, user.getFirstSchool());
            stmt.setString(9, user.getUsername());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setUserType(rs.getString("user_type"));
        user.setPhoneNumber(rs.getString("phone_number"));
        
        // Handle date of birth
        String dob = rs.getString("date_of_birth");
        if (dob != null) {
            user.setDateOfBirth(dob);
        }
        
        user.setGender(rs.getString("gender"));
        user.setInterests(rs.getString("interests"));
        user.setBio(rs.getString("bio"));
        user.setFavoriteColor(rs.getString("favorite_color"));
        user.setFirstSchool(rs.getString("first_school"));
        
        // Handle created_at if it exists in the result set
        try {
            java.sql.Date createdAt = rs.getDate("created_at");
            if (createdAt != null) {
                user.setCreatedAt(createdAt);
            }
        } catch (SQLException e) {
            // created_at column might not exist in all queries
        }
        
        return user;
    }
} 