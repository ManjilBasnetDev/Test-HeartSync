package heartsync.database;

public class DatabaseConfig {
    // Database connection constants
    public static final String DB_URL = "jdbc:mysql://localhost:3306/heartsync";
    public static final String USER = "root";
    public static final String PASS = "Rohit@56";
    public static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    // Database table names
    public static final String USERS_TABLE = "users";
    public static final String HOBBIES_TABLE = "user_hobbies";
    
    // Database creation queries
    public static final String CREATE_USERS_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
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
            relation_choice VARCHAR(50)
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
} 