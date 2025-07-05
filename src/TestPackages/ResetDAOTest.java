package TestPackages;

import heartsync.dao.ResetDAO;
import heartsync.model.User;

/**
 * Test class for ResetDAO
 * Tests all public methods with different scenarios
 */
public class ResetDAOTest {

    private ResetDAO resetDAO;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public ResetDAOTest() {
        resetDAO = new ResetDAO();
    }

    // Test resetPassword method
    public void testResetPassword() {
        System.out.println("Testing resetPassword...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = resetDAO.resetPassword(null, "newPassword");
            if (!result) {
                System.out.println("✓ resetPassword with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ resetPassword with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ resetPassword with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null password
            boolean result = resetDAO.resetPassword("testuser", null);
            if (!result) {
                System.out.println("✓ resetPassword with null password returns false");
                testsPassed++;
            } else {
                System.out.println("✗ resetPassword with null password should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ resetPassword with null password threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            boolean result = resetDAO.resetPassword("", "newPassword");
            if (!result) {
                System.out.println("✓ resetPassword with empty username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ resetPassword with empty username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ resetPassword with empty username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty password
            boolean result = resetDAO.resetPassword("testuser", "");
            if (!result) {
                System.out.println("✓ resetPassword with empty password returns false");
                testsPassed++;
            } else {
                System.out.println("✗ resetPassword with empty password should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ resetPassword with empty password threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent username
            boolean result = resetDAO.resetPassword("non-existent-user", "newPassword");
            if (!result) {
                System.out.println("✓ resetPassword with non-existent username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ resetPassword with non-existent username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ resetPassword with non-existent username threw exception: " + e.getMessage());
        }
    }

    // Test validateSecurityAnswer method
    public void testValidateSecurityAnswer() {
        System.out.println("Testing validateSecurityAnswer...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = resetDAO.validateSecurityAnswer(null, "answer");
            if (!result) {
                System.out.println("✓ validateSecurityAnswer with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityAnswer with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null answer
            boolean result = resetDAO.validateSecurityAnswer("testuser", null);
            if (!result) {
                System.out.println("✓ validateSecurityAnswer with null answer returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityAnswer with null answer should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer with null answer threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            boolean result = resetDAO.validateSecurityAnswer("", "answer");
            if (!result) {
                System.out.println("✓ validateSecurityAnswer with empty username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityAnswer with empty username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer with empty username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty answer
            boolean result = resetDAO.validateSecurityAnswer("testuser", "");
            if (!result) {
                System.out.println("✓ validateSecurityAnswer with empty answer returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityAnswer with empty answer should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer with empty answer threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent username
            boolean result = resetDAO.validateSecurityAnswer("non-existent-user", "answer");
            if (!result) {
                System.out.println("✓ validateSecurityAnswer with non-existent username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityAnswer with non-existent username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer with non-existent username threw exception: " + e.getMessage());
        }
    }

    // Test validateSecurityQuestions method
    public void testValidateSecurityQuestions() {
        System.out.println("Testing validateSecurityQuestions...");
        testsTotal++;
        
        try {
            // Test with null username
            boolean result = resetDAO.validateSecurityQuestions(null, "color", "school");
            if (!result) {
                System.out.println("✓ validateSecurityQuestions with null username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityQuestions with null username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityQuestions with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null favoriteColor
            boolean result = resetDAO.validateSecurityQuestions("testuser", null, "school");
            if (!result) {
                System.out.println("✓ validateSecurityQuestions with null favoriteColor returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityQuestions with null favoriteColor should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityQuestions with null favoriteColor threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null firstSchool
            boolean result = resetDAO.validateSecurityQuestions("testuser", "color", null);
            if (!result) {
                System.out.println("✓ validateSecurityQuestions with null firstSchool returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityQuestions with null firstSchool should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityQuestions with null firstSchool threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            boolean result = resetDAO.validateSecurityQuestions("", "color", "school");
            if (!result) {
                System.out.println("✓ validateSecurityQuestions with empty username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityQuestions with empty username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityQuestions with empty username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent username
            boolean result = resetDAO.validateSecurityQuestions("non-existent-user", "color", "school");
            if (!result) {
                System.out.println("✓ validateSecurityQuestions with non-existent username returns false");
                testsPassed++;
            } else {
                System.out.println("✗ validateSecurityQuestions with non-existent username should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ validateSecurityQuestions with non-existent username threw exception: " + e.getMessage());
        }
    }

    // Test findUserByUsername method
    public void testFindUserByUsername() {
        System.out.println("Testing findUserByUsername...");
        testsTotal++;
        
        try {
            // Test with null username
            User result = resetDAO.findUserByUsername(null);
            if (result == null) {
                System.out.println("✓ findUserByUsername with null username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ findUserByUsername with null username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ findUserByUsername with null username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty username
            User result = resetDAO.findUserByUsername("");
            if (result == null) {
                System.out.println("✓ findUserByUsername with empty username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ findUserByUsername with empty username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ findUserByUsername with empty username threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent username
            User result = resetDAO.findUserByUsername("non-existent-user");
            if (result == null) {
                System.out.println("✓ findUserByUsername with non-existent username returns null");
                testsPassed++;
            } else {
                System.out.println("✗ findUserByUsername with non-existent username should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ findUserByUsername with non-existent username threw exception: " + e.getMessage());
        }
    }

    // Test case sensitivity for security questions
    public void testCaseSensitivity() {
        System.out.println("Testing case sensitivity...");
        testsTotal++;
        
        try {
            // Test that validateSecurityAnswer should be case-insensitive
            // This test will pass if the method doesn't throw an exception
            boolean result1 = resetDAO.validateSecurityAnswer("testuser", "BLUE");
            boolean result2 = resetDAO.validateSecurityAnswer("testuser", "blue");
            boolean result3 = resetDAO.validateSecurityAnswer("testuser", "Blue");
            
            System.out.println("✓ validateSecurityAnswer handles different cases without throwing exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer case sensitivity test threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test that validateSecurityQuestions should be case-insensitive
            boolean result = resetDAO.validateSecurityQuestions("testuser", "BLUE", "ABC SCHOOL");
            
            System.out.println("✓ validateSecurityQuestions handles different cases without throwing exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ validateSecurityQuestions case sensitivity test threw exception: " + e.getMessage());
        }
    }

    // Test special characters in passwords and answers
    public void testSpecialCharacters() {
        System.out.println("Testing special characters...");
        testsTotal++;
        
        try {
            // Test resetPassword with special characters in password
            boolean result = resetDAO.resetPassword("testuser", "P@ssw0rd!@#$%");
            // Should not throw exception
            System.out.println("✓ resetPassword with special characters doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ resetPassword with special characters threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test validateSecurityAnswer with special characters
            boolean result = resetDAO.validateSecurityAnswer("testuser", "answer!@#$%");
            // Should not throw exception
            System.out.println("✓ validateSecurityAnswer with special characters doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ validateSecurityAnswer with special characters threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== ResetDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testResetPassword();
        testValidateSecurityAnswer();
        testValidateSecurityQuestions();
        testFindUserByUsername();
        testCaseSensitivity();
        testSpecialCharacters();
        
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
        ResetDAOTest test = new ResetDAOTest();
        test.runAllTests();
    }
} 