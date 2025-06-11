package heartsync.database;

public class DatabaseConfig {
    // Database connection settings
    public static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/datingapp";
    public static final String USER = "manjil";
    public static final String PASS = "3023";
    
    // Table names
    public static final String USERS_TABLE = "users";
    public static final String PROFILES_TABLE = "user_profiles";
    public static final String MATCHES_TABLE = "matches";
    public static final String MESSAGES_TABLE = "messages";
    public static final String NOTIFICATIONS_TABLE = "notifications";
    
    // SQL Queries
    public static final String GET_USER_BY_USERNAME = "SELECT * FROM " + USERS_TABLE + " WHERE username = ?";
    public static final String GET_USER_BY_ID = "SELECT * FROM " + USERS_TABLE + " WHERE id = ?";
    public static final String UPDATE_USER_PASSWORD = "UPDATE " + USERS_TABLE + " SET password = ? WHERE id = ?";
    public static final String VERIFY_SECURITY_QUESTIONS = "SELECT * FROM " + USERS_TABLE + 
            " WHERE username = ? AND favorite_color = ? AND first_school = ?";
    
    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }
}