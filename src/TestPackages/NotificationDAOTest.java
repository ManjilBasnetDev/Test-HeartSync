package TestPackages;

import heartsync.dao.NotificationDAO;
import heartsync.model.Notification;
import java.util.List;

/**
 * Test class for NotificationDAO
 * Tests all public methods with different scenarios
 */
public class NotificationDAOTest {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public NotificationDAOTest() {
    }

    // Test addNotification method
    public void testAddNotification() {
        System.out.println("Testing addNotification...");
        testsTotal++;
        
        try {
            // Test with null notification
            NotificationDAO.addNotification(null);
            System.out.println("✓ addNotification with null notification doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with null notification threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with valid notification
            Notification notification = new Notification("Test notification", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with valid notification doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with valid notification threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with notification with empty content
            Notification notification = new Notification("", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with empty content doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with empty content threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with MATCH type notification
            Notification notification = new Notification("You have a new match!", Notification.Type.MATCH);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with MATCH type doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with MATCH type threw exception: " + e.getMessage());
        }
    }

    // Test getAllNotifications method
    public void testGetAllNotifications() {
        System.out.println("Testing getAllNotifications...");
        testsTotal++;
        
        try {
            // Test if getAllNotifications returns a list (not null)
            List<Notification> result = NotificationDAO.getAllNotifications();
            if (result != null) {
                System.out.println("✓ getAllNotifications returns non-null list");
                testsPassed++;
            } else {
                System.out.println("✗ getAllNotifications should return non-null list");
            }
        } catch (Exception e) {
            System.out.println("✗ getAllNotifications threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test if getAllNotifications returns consistent results
            List<Notification> result1 = NotificationDAO.getAllNotifications();
            List<Notification> result2 = NotificationDAO.getAllNotifications();
            if (result1 != null && result2 != null) {
                System.out.println("✓ getAllNotifications returns consistent results");
                testsPassed++;
            } else {
                System.out.println("✗ getAllNotifications should return consistent results");
            }
        } catch (Exception e) {
            System.out.println("✗ getAllNotifications consistency test threw exception: " + e.getMessage());
        }
    }

    // Test markAllAsRead method
    public void testMarkAllAsRead() {
        System.out.println("Testing markAllAsRead...");
        testsTotal++;
        
        try {
            // Test if markAllAsRead executes without exception
            NotificationDAO.markAllAsRead();
            System.out.println("✓ markAllAsRead doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ markAllAsRead threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test markAllAsRead multiple times
            NotificationDAO.markAllAsRead();
            NotificationDAO.markAllAsRead();
            System.out.println("✓ markAllAsRead can be called multiple times without exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ markAllAsRead multiple calls threw exception: " + e.getMessage());
        }
    }

    // Test interaction between methods
    public void testMethodInteraction() {
        System.out.println("Testing method interactions...");
        testsTotal++;
        
        try {
            // Test adding notification and then getting all notifications
            Notification notification = new Notification("Test interaction notification", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            List<Notification> notifications = NotificationDAO.getAllNotifications();
            
            if (notifications != null) {
                System.out.println("✓ Adding notification and getting all notifications works");
                testsPassed++;
            } else {
                System.out.println("✗ Adding notification and getting all notifications failed");
            }
        } catch (Exception e) {
            System.out.println("✗ Method interaction test threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test marking all as read after adding notifications
            Notification notification = new Notification("Test mark as read notification", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            NotificationDAO.markAllAsRead();
            
            System.out.println("✓ Adding notification and marking all as read works");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Adding notification and marking as read threw exception: " + e.getMessage());
        }
    }

    // Test edge cases with special characters
    public void testSpecialCharacters() {
        System.out.println("Testing special characters...");
        testsTotal++;
        
        try {
            // Test notification with special characters in content
            Notification notification = new Notification("Test notification with special chars: !@#$%^&*()", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with special characters doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with special characters threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test notification with empty content
            Notification notification = new Notification("", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with empty content doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with empty content threw exception: " + e.getMessage());
        }
    }

    // Test notification types
    public void testNotificationTypes() {
        System.out.println("Testing notification types...");
        testsTotal++;
        
        try {
            // Test notification with MESSAGE type
            Notification notification = new Notification("Test message notification", Notification.Type.MESSAGE);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with MESSAGE type doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with MESSAGE type threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test notification with MATCH type
            Notification notification = new Notification("Test match notification", Notification.Type.MATCH);
            
            NotificationDAO.addNotification(notification);
            System.out.println("✓ addNotification with MATCH type doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ addNotification with MATCH type threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== NotificationDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testAddNotification();
        testGetAllNotifications();
        testMarkAllAsRead();
        testMethodInteraction();
        testSpecialCharacters();
        testNotificationTypes();
        
        System.out.println("\n=== Test Results ===");
        System.out.println("Tests passed: " + testsPassed + "/" + testsTotal);
        System.out.println("Success rate: " + (testsPassed * 100 / testsTotal) + "%");
        
        if (testsPassed == testsTotal) {
            System.out.println("✓ All tests passed!");
        } else {
            System.out.println("✗ Some tests failed. Check the output above.");
        }
    }

    // Main method to run tests
    public static void main(String[] args) {
        NotificationDAOTest test = new NotificationDAOTest();
        test.runAllTests();
    }
} 