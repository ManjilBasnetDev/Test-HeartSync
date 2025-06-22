package heartsync.database;

/**
 * Utility class to remove all test/demo users from the HeartSync system.
 * This class provides a comprehensive cleanup that removes test users from:
 * - Dating database (profiles, likes, matches, notifications)
 * - Authentication system
 * - User profile details
 * - Reports and admin data
 * 
 * Can be run independently or called from within the application.
 */
public class TestUserCleanup {
    
    /**
     * Main method to run the cleanup independently
     */
    public static void main(String[] args) {
        System.out.println("HeartSync Test User Cleanup Utility");
        System.out.println("===================================");
        
        try {
            runCleanup();
            System.out.println("\nCleanup completed successfully!");
            System.out.println("You can now run your application with clean data.");
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Runs the comprehensive test user cleanup
     */
    public static void runCleanup() {
        DatingDatabase database = DatingDatabase.getInstance();
        database.removeAllTestUsersFromSystem();
    }
    
    /**
     * Runs cleanup silently (for application startup)
     */
    public static void runSilentCleanup() {
        try {
            System.out.println("Running silent test user cleanup...");
            DatingDatabase database = DatingDatabase.getInstance();
            database.removeAllTestUsersFromSystem();
            System.out.println("Silent cleanup completed.");
        } catch (Exception e) {
            System.err.println("Silent cleanup failed: " + e.getMessage());
            // Don't crash the application, just log the error
        }
    }
} 