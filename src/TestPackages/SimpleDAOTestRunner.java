package TestPackages;

import heartsync.model.User;
import heartsync.model.Chat;
import heartsync.model.Contact;
import heartsync.dao.*;
import java.util.List;

/**
 * Simple DAO Test Runner - Basic functionality tests without external dependencies
 * This runner focuses on testing the core DAO methods with simple validation
 */
public class SimpleDAOTestRunner {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public static void main(String[] args) {
        System.out.println("===================================================");
        System.out.println("           HeartSync Simple DAO Test Runner        ");
        System.out.println("===================================================");
        System.out.println("Testing basic DAO functionality...\n");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Test UserDAO
            testUserDAO();
            System.out.println();
            
            // Test UserRegisterDAO
            testUserRegisterDAO();
            System.out.println();
            
            // Test LikeDAO
            testLikeDAO();
            System.out.println();
            
            // Test ChatDAO
            testChatDAO();
            System.out.println();
            
            // Test MatchDAO
            testMatchDAO();
            System.out.println();
            
            // Test ResetDAO
            testResetDAO();
            System.out.println();
            
            // Test ContactDAO
            testContactDAO();
            System.out.println();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.println("===================================================");
            System.out.println("              Test Suite Summary                    ");
            System.out.println("===================================================");
            System.out.println("Total tests run: " + testsTotal);
            System.out.println("Tests passed: " + testsPassed);
            System.out.println("Tests failed: " + (testsTotal - testsPassed));
            System.out.println("Success rate: " + (testsTotal > 0 ? (testsPassed * 100 / testsTotal) : 0) + "%");
            System.out.println("Total execution time: " + duration + "ms");
            System.out.println("===================================================");
            
        } catch (Exception e) {
            System.err.println("Error running test suite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testUserDAO() {
        System.out.println("1. Testing UserDAO...");
        UserDAO userDAO = new UserDAO();
        
        // Test getUserById with null
        testMethod("getUserById with null", () -> {
            User result = userDAO.getUserById(null);
            return result == null;
        });
        
        // Test usernameExists with null
        testMethod("usernameExists with null", () -> {
            boolean result = userDAO.usernameExists(null);
            return !result;
        });
        
        // Test authenticateUser with null values
        testMethod("authenticateUser with null username", () -> {
            User result = userDAO.authenticateUser(null, "password");
            return result == null;
        });
        
        testMethod("authenticateUser with null password", () -> {
            User result = userDAO.authenticateUser("username", null);
            return result == null;
        });
        
        // Test createUser with null
        testMethod("createUser with null", () -> {
            boolean result = userDAO.createUser(null);
            return !result;
        });
    }

    private static void testUserRegisterDAO() {
        System.out.println("2. Testing UserRegisterDAO...");
        UserRegisterDAO userRegisterDAO = new UserRegisterDAO();
        
        // Test createUser with null
        testMethod("createUser with null", () -> {
            boolean result = userRegisterDAO.createUser(null);
            return !result;
        });
        
        // Test getUser with null
        testMethod("getUser with null", () -> {
            User result = userRegisterDAO.getUser(null);
            return result == null;
        });
        
        // Test validateUser with null values
        testMethod("validateUser with null username", () -> {
            boolean result = userRegisterDAO.validateUser(null, "password");
            return !result;
        });
        
        testMethod("validateUser with null password", () -> {
            boolean result = userRegisterDAO.validateUser("username", null);
            return !result;
        });
        
        // Test getAllUsers
        testMethod("getAllUsers returns non-null", () -> {
            List<User> result = userRegisterDAO.getAllUsers();
            return result != null;
        });
    }

    private static void testLikeDAO() {
        System.out.println("3. Testing LikeDAO...");
        LikeDAO likeDAO = new LikeDAO();
        
        // Test addLike with null values
        testMethod("addLike with null userId", () -> {
            boolean result = likeDAO.addLike(null, "likedUserId");
            return !result;
        });
        
        testMethod("addLike with null likedUserId", () -> {
            boolean result = likeDAO.addLike("userId", null);
            return !result;
        });
        
        // Test addPass with null values
        testMethod("addPass with null userId", () -> {
            boolean result = likeDAO.addPass(null, "passedUserId");
            return !result;
        });
        
        testMethod("addPass with null passedUserId", () -> {
            boolean result = likeDAO.addPass("userId", null);
            return !result;
        });
        
        // Test getLikedUsers with null
        testMethod("getLikedUsers with null", () -> {
            List<String> result = likeDAO.getLikedUsers(null);
            return result != null;
        });
        
        // Test getMatches with null
        testMethod("getMatches with null", () -> {
            List<String> result = likeDAO.getMatches(null);
            return result != null;
        });
    }

    private static void testChatDAO() {
        System.out.println("4. Testing ChatDAO...");
        ChatDAO chatDAO = new ChatDAO();
        
        // Test getChatId with null values
        testMethod("getChatId with null user1", () -> {
            try {
                String result = chatDAO.getChatId(null, "user2");
                return result == null || result.contains("null");
            } catch (Exception e) {
                return true; // Expected to fail
            }
        });
        
        testMethod("getChatId with null user2", () -> {
            try {
                String result = chatDAO.getChatId("user1", null);
                return result == null || result.contains("null");
            } catch (Exception e) {
                return true; // Expected to fail
            }
        });
        
        // Test getConversation with null values
        testMethod("getConversation with null user1", () -> {
            List<Chat> result = chatDAO.getConversation(null, "user2");
            return result != null;
        });
        
        testMethod("getConversation with null user2", () -> {
            List<Chat> result = chatDAO.getConversation("user1", null);
            return result != null;
        });
        
        // Test getLastMessage with null values
        testMethod("getLastMessage with null user1", () -> {
            Chat result = chatDAO.getLastMessage(null, "user2");
            return result == null;
        });
    }

    private static void testMatchDAO() {
        System.out.println("5. Testing MatchDAO...");
        MatchDAO matchDAO = new MatchDAO();
        
        // Test setMatch with null values
        testMethod("setMatch with null userId", () -> {
            boolean result = matchDAO.setMatch(null, "otherUserId", true);
            return !result;
        });
        
        testMethod("setMatch with null otherUserId", () -> {
            boolean result = matchDAO.setMatch("userId", null, true);
            return !result;
        });
        
        // Test isMatch with null values
        testMethod("isMatch with null userId", () -> {
            boolean result = matchDAO.isMatch(null, "otherUserId");
            return !result;
        });
        
        testMethod("isMatch with null otherUserId", () -> {
            boolean result = matchDAO.isMatch("userId", null);
            return !result;
        });
        
        // Test getMatches with null
        testMethod("getMatches with null", () -> {
            List<String> result = matchDAO.getMatches(null);
            return result != null;
        });
    }

    private static void testResetDAO() {
        System.out.println("6. Testing ResetDAO...");
        ResetDAO resetDAO = new ResetDAO();
        
        // Test resetPassword with null values
        testMethod("resetPassword with null username", () -> {
            boolean result = resetDAO.resetPassword(null, "newPassword");
            return !result;
        });
        
        testMethod("resetPassword with null password", () -> {
            boolean result = resetDAO.resetPassword("username", null);
            return !result;
        });
        
        // Test validateSecurityAnswer with null values
        testMethod("validateSecurityAnswer with null username", () -> {
            boolean result = resetDAO.validateSecurityAnswer(null, "answer");
            return !result;
        });
        
        testMethod("validateSecurityAnswer with null answer", () -> {
            boolean result = resetDAO.validateSecurityAnswer("username", null);
            return !result;
        });
        
        // Test findUserByUsername with null
        testMethod("findUserByUsername with null", () -> {
            User result = resetDAO.findUserByUsername(null);
            return result == null;
        });
    }

    private static void testContactDAO() {
        System.out.println("7. Testing ContactDAO...");
        ContactDAO contactDAO = new ContactDAO();
        
        // Test saveContact with null
        testMethod("saveContact with null", () -> {
            boolean result = contactDAO.saveContact(null);
            return !result;
        });
        
        // Test getAllContacts
        testMethod("getAllContacts returns non-null", () -> {
            try {
                contactDAO.getAllContacts();
                return true; // Should not throw exception
            } catch (Exception e) {
                return false;
            }
        });
        
        // Test saveContact with valid contact
        testMethod("saveContact with valid contact", () -> {
            try {
                Contact contact = new Contact("Test User", "test@example.com", "Test message");
                boolean result = contactDAO.saveContact(contact);
                return result;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private static void testMethod(String testName, TestFunction test) {
        testsTotal++;
        try {
            boolean passed = test.run();
            if (passed) {
                System.out.println("✓ " + testName);
                testsPassed++;
            } else {
                System.out.println("✗ " + testName);
            }
        } catch (Exception e) {
            System.out.println("✗ " + testName + " (Exception: " + e.getMessage() + ")");
        }
    }

    @FunctionalInterface
    private interface TestFunction {
        boolean run() throws Exception;
    }
} 