package heartsync.database;

public class DatabaseCleanup {
    public static void main(String[] args) {
        try {
            System.out.println("Starting database cleanup...");
            FirebaseConfig.deleteAllData();
            System.out.println("Database cleanup completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during database cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 