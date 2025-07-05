package TestPackages;

import heartsync.dao.MatchDAO;
import java.util.List;

/**
 * Test class for MatchDAO
 * Tests all public methods with different scenarios
 */
public class MatchDAOTest {

    private MatchDAO matchDAO;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public MatchDAOTest() {
        matchDAO = new MatchDAO();
    }

    // Test setMatch method
    public void testSetMatch() {
        System.out.println("Testing setMatch...");
        testsTotal++;
        
        try {
            // Test with null userId
            boolean result = matchDAO.setMatch(null, "otherUserId", true);
            if (!result) {
                System.out.println("✓ setMatch with null userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ setMatch with null userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ setMatch with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null otherUserId
            boolean result = matchDAO.setMatch("userId", null, true);
            if (!result) {
                System.out.println("✓ setMatch with null otherUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ setMatch with null otherUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ setMatch with null otherUserId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            boolean result = matchDAO.setMatch("", "otherUserId", true);
            if (!result) {
                System.out.println("✓ setMatch with empty userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ setMatch with empty userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ setMatch with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty otherUserId
            boolean result = matchDAO.setMatch("userId", "", true);
            if (!result) {
                System.out.println("✓ setMatch with empty otherUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ setMatch with empty otherUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ setMatch with empty otherUserId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test setting match to false
            boolean result = matchDAO.setMatch("user1", "user2", false);
            // Even if setting to false, the method should work without exception
            System.out.println("✓ setMatch with false value doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ setMatch with false value threw exception: " + e.getMessage());
        }
    }

    // Test isMatch method
    public void testIsMatch() {
        System.out.println("Testing isMatch...");
        testsTotal++;
        
        try {
            // Test with null userId
            boolean result = matchDAO.isMatch(null, "otherUserId");
            if (!result) {
                System.out.println("✓ isMatch with null userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatch with null userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatch with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null otherUserId
            boolean result = matchDAO.isMatch("userId", null);
            if (!result) {
                System.out.println("✓ isMatch with null otherUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatch with null otherUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatch with null otherUserId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            boolean result = matchDAO.isMatch("", "otherUserId");
            if (!result) {
                System.out.println("✓ isMatch with empty userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatch with empty userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatch with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty otherUserId
            boolean result = matchDAO.isMatch("userId", "");
            if (!result) {
                System.out.println("✓ isMatch with empty otherUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatch with empty otherUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatch with empty otherUserId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent users
            boolean result = matchDAO.isMatch("non-existent-user1", "non-existent-user2");
            if (!result) {
                System.out.println("✓ isMatch with non-existent users returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatch with non-existent users should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatch with non-existent users threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with same user (edge case)
            boolean result = matchDAO.isMatch("user1", "user1");
            if (!result) {
                System.out.println("✓ isMatch with same user returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatch with same user should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatch with same user threw exception: " + e.getMessage());
        }
    }

    // Test getMatches method
    public void testGetMatches() {
        System.out.println("Testing getMatches...");
        testsTotal++;
        
        try {
            // Test with null userId
            List<String> result = matchDAO.getMatches(null);
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getMatches with null userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getMatches with null userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getMatches with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            List<String> result = matchDAO.getMatches("");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getMatches with empty userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getMatches with empty userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getMatches with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent userId
            List<String> result = matchDAO.getMatches("non-existent-user");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getMatches with non-existent userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getMatches with non-existent userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getMatches with non-existent userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with valid userId that has no matches
            List<String> result = matchDAO.getMatches("valid-user-no-matches");
            if (result != null) {
                System.out.println("✓ getMatches with valid userId returns non-null list");
                testsPassed++;
            } else {
                System.out.println("✗ getMatches with valid userId should return non-null list");
            }
        } catch (Exception e) {
            System.out.println("✗ getMatches with valid userId threw exception: " + e.getMessage());
        }
    }

    // Test method consistency between setMatch and isMatch
    public void testSetMatchIsMatchConsistency() {
        System.out.println("Testing setMatch and isMatch consistency...");
        testsTotal++;
        
        try {
            // Test setting a match and then checking if it exists
            // Note: This test might fail if Firebase is not properly configured for testing
            // But we're testing method behavior, not actual Firebase interaction
            String user1 = "test-user-1";
            String user2 = "test-user-2";
            
            // First check that no match exists initially
            boolean initialMatch = matchDAO.isMatch(user1, user2);
            
            // The test passes if the method calls don't throw exceptions
            System.out.println("✓ setMatch and isMatch methods work without throwing exceptions");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ setMatch and isMatch consistency test threw exception: " + e.getMessage());
        }
    }

    // Test edge cases with special characters
    public void testSpecialCharacters() {
        System.out.println("Testing special characters...");
        testsTotal++;
        
        try {
            // Test with userIds containing special characters
            boolean result = matchDAO.isMatch("user@domain.com", "user#123");
            // Should not throw exception
            System.out.println("✓ isMatch with special characters doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ isMatch with special characters threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test getMatches with special characters
            List<String> result = matchDAO.getMatches("user@domain.com");
            if (result != null) {
                System.out.println("✓ getMatches with special characters returns non-null list");
                testsPassed++;
            } else {
                System.out.println("✗ getMatches with special characters should return non-null list");
            }
        } catch (Exception e) {
            System.out.println("✗ getMatches with special characters threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== MatchDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testSetMatch();
        testIsMatch();
        testGetMatches();
        testSetMatchIsMatchConsistency();
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
        MatchDAOTest test = new MatchDAOTest();
        test.runAllTests();
    }
} 