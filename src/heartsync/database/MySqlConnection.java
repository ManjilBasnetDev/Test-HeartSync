package heartsync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    public static boolean testConnection() {
        // Dummy implementation: always returns true
        return true;
    }

    public static Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
