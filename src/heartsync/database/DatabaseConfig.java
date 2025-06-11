package heartsync.database;

public class DatabaseConfig {
    // Database connection constants
    public static final String DB_URL = "jdbc:mysql://localhost:3306/heartsync";
    public static final String USER = "root";
    public static final String PASS = "lokeshsingh9841@";
    public static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    // Database table names
    public static final String USERS_TABLE = "users";
    public static final String PROFILES_TABLE = "user_profiles";
    public static final String HOBBIES_TABLE = "user_hobbies";
    
    // Database table schemas
    public static final String CREATE_USERS_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL,
            user_type ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
            email VARCHAR(100),
            phone_number VARCHAR(20),
            date_of_birth DATE,
            gender VARCHAR(10),
            interests TEXT,
            bio TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
    """;
    
    public static final String CREATE_PROFILES_TABLE = """
        CREATE TABLE IF NOT EXISTS user_profiles (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            full_name VARCHAR(100),
            height INT,
            weight INT,
            country VARCHAR(100),
            address TEXT,
            phone VARCHAR(20),
            qualification VARCHAR(50),
            gender VARCHAR(20),
            preferences VARCHAR(20),
            about_me TEXT,
            profile_pic_path VARCHAR(255),
            relation_choice VARCHAR(50),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
    """;
    
    public static final String CREATE_HOBBIES_TABLE = """
        CREATE TABLE IF NOT EXISTS user_hobbies (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT,
            hobby VARCHAR(100),
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
    """;
    
    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }
} 