package TestPackages;

import heartsync.model.User;
import heartsync.dao.UserDAO;

/**
 * Test class for UserDAO
 * Tests all public methods with different scenarios
 */
public class UserDAOTest {

    private UserDAO userDAO;
    private User testUser;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public UserDAOTest() {
        userDAO = new UserDAO();
        setupTestData();
    }

    private void setupTestData() {
        testUser = new User();
        testUser.setUserId("test-user-id");
        testUser.setUsername("testuser");
        testUser.setPassword("testpassword");
        testUser.setEmail("test@email.com");
        testUser.setFullName("Test User");
        testUser.setSecurityQuestion("What is your favorite color?");
        testUser.setSecurityAnswer("blue");
        testUser.setSecurityQuestion2("What is your first school?");
        testUser.setSecurityAnswer2("ABC School");
        testUser.setFavoriteColor("blue");
        testUser.setFirstSchool("ABC School");
    }

    // Test getUserById method
    public void testGetUserById() {
        System.out.println("Testing getUserById...");
        testsTotal++;
        
        try {
            // Test with null ID
            User result = userDAO.getUserById(null);
            if (result == null) {
                System.out.println("✓ getUserById with null ID returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUserById with null ID should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUserById with null ID threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent ID
            User result = userDAO.getUserById("non-existent-id");
            if (result == null) {
                System.out.println("✓ getUserById with non-existent ID returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUserById with non-existent ID should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUserById with non-existent ID threw exception: " + e.getMessage());
        }
    }

    // Test authenticateUser method
    public void testAuthenticateUser() {
        System.out.println("Testing authenticateUser...");
        testsTotal++;
        
        try {
            // Test with null username
            User result = userDAO.authenticateUser(null, "password");
            if (result == null) {
                System.out.println("✓ authenticateUser with null username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ authenticateUser with null username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ authenticateUser with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null password
            User result = userDAO.authenticateUser("testuser", null);
            if (result == null) {
                System.out.println("✓ authenticateUser with null password returns null");
                testsPassed++;
            } else {
                System.out.println("✗ authenticateUser with null password should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ authenticateUser with null password threw exception: " + e.getMessage());
        }
    }

    // Test createUser method
    public void testCreateUser() {
        System.out.println("Testing createUser...");
        testsTotal++;
        
        try {
            // Test with null user
            boolean result = userDAO.createUser(null);
            if (!result) {
                System.out.println("✓ createUser with null user returns false");
                testsPassed++;
            } else {
                System.out.println("✗ createUser with null user should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ createUser with null user threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with user with no username
            User userWithoutUsername = new User();
            userWithoutUsername.setPassword("password");
            boolean result = userDAO.createUser(userWithoutUsername);
            if (!result) {
                System.out.println("✓ createUser with user without username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ createUser with user without username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ createUser with user without username threw exception: " + e.getMessage());
        }
    }

    // Test usernameExists method
    public void testUsernameExists() {
        System.out.println("Testing usernameExists...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = userDAO.usernameExists(null);
            if (!result) {
                System.out.println("✓ usernameExists with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ usernameExists with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ usernameExists with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            boolean result = userDAO.usernameExists("");
            if (!result) {
                System.out.println("✓ usernameExists with empty username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ usernameExists with empty username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ usernameExists with empty username threw exception: " + e.getMessage());
        }
    }

    // Test getUserByUsername method
    public void testGetUserByUsername() {
        System.out.println("Testing getUserByUsername...");
        testsTotal++;
        
        try {
            // Test with null username
            User result = userDAO.getUserByUsername(null);
            if (result == null) {
                System.out.println("✓ getUserByUsername with null username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUserByUsername with null username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUserByUsername with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            User result = userDAO.getUserByUsername("");
            if (result == null) {
                System.out.println("✓ getUserByUsername with empty username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUserByUsername with empty username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUserByUsername with empty username threw exception: " + e.getMessage());
        }
    }

    // Test updatePassword method
    public void testUpdatePassword() {
        System.out.println("Testing updatePassword...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = userDAO.updatePassword(null, "newpassword");
            if (!result) {
                System.out.println("✓ updatePassword with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ updatePassword with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ updatePassword with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null password
            boolean result = userDAO.updatePassword("testuser", null);
            if (!result) {
                System.out.println("✓ updatePassword with null password returns false");
                testsPassed++;
            } else {
                System.out.println("✗ updatePassword with null password should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ updatePassword with null password threw exception: " + e.getMessage());
        }
    }

    // Test deleteUser method
    public void testDeleteUser() {
        System.out.println("Testing deleteUser...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = userDAO.deleteUser(null);
            if (!result) {
                System.out.println("✓ deleteUser with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ deleteUser with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ deleteUser with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            boolean result = userDAO.deleteUser("");
            if (!result) {
                System.out.println("✓ deleteUser with empty username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ deleteUser with empty username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ deleteUser with empty username threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== UserDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testGetUserById();
        testAuthenticateUser();
        testCreateUser();
        testUsernameExists();
        testGetUserByUsername();
        testUpdatePassword();
        testDeleteUser();
        
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
        UserDAOTest test = new UserDAOTest();
        test.runAllTests();
    }
} 