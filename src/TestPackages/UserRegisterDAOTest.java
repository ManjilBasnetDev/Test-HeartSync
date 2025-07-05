package TestPackages;

import heartsync.model.User;
import heartsync.dao.UserRegisterDAO;
import java.util.List;

/**
 * Test class for UserRegisterDAO
 * Tests all public methods with different scenarios
 */
public class UserRegisterDAOTest {

    private UserRegisterDAO userRegisterDAO;
    private User testUser;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public UserRegisterDAOTest() {
        userRegisterDAO = new UserRegisterDAO();
        setupTestData();
    }

    private void setupTestData() {
        testUser = new User();
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

    // Test createUser method
    public void testCreateUser() {
        System.out.println("Testing createUser...");
        testsTotal++;
        
        try {
            // Test with null user
            boolean result = userRegisterDAO.createUser(null);
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
            // Test with user missing username
            User userWithoutUsername = new User();
            userWithoutUsername.setPassword("password");
            userWithoutUsername.setSecurityQuestion("question");
            userWithoutUsername.setSecurityAnswer("answer");
            userWithoutUsername.setSecurityQuestion2("question2");
            userWithoutUsername.setSecurityAnswer2("answer2");
            
            boolean result = userRegisterDAO.createUser(userWithoutUsername);
            if (!result) {
                System.out.println("✓ createUser with missing username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ createUser with missing username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ createUser with missing username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with user missing password
            User userWithoutPassword = new User();
            userWithoutPassword.setUsername("testuser");
            userWithoutPassword.setSecurityQuestion("question");
            userWithoutPassword.setSecurityAnswer("answer");
            userWithoutPassword.setSecurityQuestion2("question2");
            userWithoutPassword.setSecurityAnswer2("answer2");
            
            boolean result = userRegisterDAO.createUser(userWithoutPassword);
            if (!result) {
                System.out.println("✓ createUser with missing password returns false");
                testsPassed++;
            } else {
                System.out.println("✗ createUser with missing password should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ createUser with missing password threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with user missing security question
            User userWithoutSecurityQuestion = new User();
            userWithoutSecurityQuestion.setUsername("testuser");
            userWithoutSecurityQuestion.setPassword("password");
            userWithoutSecurityQuestion.setSecurityAnswer("answer");
            userWithoutSecurityQuestion.setSecurityQuestion2("question2");
            userWithoutSecurityQuestion.setSecurityAnswer2("answer2");
            
            boolean result = userRegisterDAO.createUser(userWithoutSecurityQuestion);
            if (!result) {
                System.out.println("✓ createUser with missing security question returns false");
                testsPassed++;
            } else {
                System.out.println("✗ createUser with missing security question should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ createUser with missing security question threw exception: " + e.getMessage());
        }
    }

    // Test getUser method
    public void testGetUser() {
        System.out.println("Testing getUser...");
        testsTotal++;
        
        try {
            // Test with null username
            User result = userRegisterDAO.getUser(null);
            if (result == null) {
                System.out.println("✓ getUser with null username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUser with null username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUser with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            User result = userRegisterDAO.getUser("");
            if (result == null) {
                System.out.println("✓ getUser with empty username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUser with empty username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUser with empty username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent username
            User result = userRegisterDAO.getUser("nonexistentuser");
            if (result == null) {
                System.out.println("✓ getUser with non-existent username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getUser with non-existent username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getUser with non-existent username threw exception: " + e.getMessage());
        }
    }

    // Test validateUser method
    public void testValidateUser() {
        System.out.println("Testing validateUser...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = userRegisterDAO.validateUser(null, "password");
            if (!result) {
                System.out.println("✓ validateUser with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateUser with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateUser with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null password
            boolean result = userRegisterDAO.validateUser("testuser", null);
            if (!result) {
                System.out.println("✓ validateUser with null password returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateUser with null password should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateUser with null password threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent user
            boolean result = userRegisterDAO.validateUser("nonexistentuser", "password");
            if (!result) {
                System.out.println("✓ validateUser with non-existent user returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateUser with non-existent user should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateUser with non-existent user threw exception: " + e.getMessage());
        }
    }

    // Test getAllUsers method
    public void testGetAllUsers() {
        System.out.println("Testing getAllUsers...");
        testsTotal++;
        
        try {
            // Test if getAllUsers returns a list (not null)
            List<User> result = userRegisterDAO.getAllUsers();
            if (result != null) {
                System.out.println("✓ getAllUsers returns non-null list");
                testsPassed++;
            } else {
                System.out.println("✗ getAllUsers should return non-null list");
            }
        } catch (Exception e) {
            System.out.println("✗ getAllUsers threw exception: " + e.getMessage());
        }
    }

    // Test updateUser method
    public void testUpdateUser() {
        System.out.println("Testing updateUser...");
        testsTotal++;
        
        try {
            // Test with null user
            boolean result = userRegisterDAO.updateUser(null);
            if (!result) {
                System.out.println("✓ updateUser with null user returns false");
                testsPassed++;
            } else {
                System.out.println("✗ updateUser with null user should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ updateUser with null user threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with user without userId
            User userWithoutId = new User();
            userWithoutId.setUsername("testuser");
            userWithoutId.setPassword("password");
            
            boolean result = userRegisterDAO.updateUser(userWithoutId);
            if (!result) {
                System.out.println("✓ updateUser with user without userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ updateUser with user without userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ updateUser with user without userId threw exception: " + e.getMessage());
        }
    }

    // Test verifyUser method
    public void testVerifyUser() {
        System.out.println("Testing verifyUser...");
        testsTotal++;
        
        try {
            // Test with null userId
            boolean result = userRegisterDAO.verifyUser(null);
            if (!result) {
                System.out.println("✓ verifyUser with null userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ verifyUser with null userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ verifyUser with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent userId
            boolean result = userRegisterDAO.verifyUser("non-existent-id");
            if (!result) {
                System.out.println("✓ verifyUser with non-existent userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ verifyUser with non-existent userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ verifyUser with non-existent userId threw exception: " + e.getMessage());
        }
    }

    // Test rejectUser method
    public void testRejectUser() {
        System.out.println("Testing rejectUser...");
        testsTotal++;
        
        try {
            // Test with null userId
            boolean result = userRegisterDAO.rejectUser(null);
            if (!result) {
                System.out.println("✓ rejectUser with null userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ rejectUser with null userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ rejectUser with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent userId
            boolean result = userRegisterDAO.rejectUser("non-existent-id");
            if (!result) {
                System.out.println("✓ rejectUser with non-existent userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ rejectUser with non-existent userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ rejectUser with non-existent userId threw exception: " + e.getMessage());
        }
    }

    // Test getLastError method
    public void testGetLastError() {
        System.out.println("Testing getLastError...");
        testsTotal++;
        
        try {
            // Test if getLastError returns a string (not null)
            String result = userRegisterDAO.getLastError();
            if (result != null) {
                System.out.println("✓ getLastError returns non-null string");
                testsPassed++;
            } else {
                System.out.println("✗ getLastError should return non-null string");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastError threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== UserRegisterDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testCreateUser();
        testGetUser();
        testValidateUser();
        testGetAllUsers();
        testUpdateUser();
        testVerifyUser();
        testRejectUser();
        testGetLastError();
        
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
        UserRegisterDAOTest test = new UserRegisterDAOTest();
        test.runAllTests();
    }
} 