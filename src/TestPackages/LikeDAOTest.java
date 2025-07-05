package TestPackages;

import heartsync.dao.LikeDAO;
import java.util.List;

/**
 * Test class for LikeDAO
 * Tests all public methods with different scenarios
 */
public class LikeDAOTest {

    private LikeDAO likeDAO;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public LikeDAOTest() {
        likeDAO = new LikeDAO();
    }

    // Test addLike method
    public void testAddLike() {
        System.out.println("Testing addLike...");
        testsTotal++;
        
        try {
            // Test with null userId
            boolean result = likeDAO.addLike(null, "likedUserId");
            if (!result) {
                System.out.println("✓ addLike with null userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addLike with null userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addLike with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null likedUserId
            boolean result = likeDAO.addLike("userId", null);
            if (!result) {
                System.out.println("✓ addLike with null likedUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addLike with null likedUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addLike with null likedUserId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            boolean result = likeDAO.addLike("", "likedUserId");
            if (!result) {
                System.out.println("✓ addLike with empty userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addLike with empty userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addLike with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty likedUserId
            boolean result = likeDAO.addLike("userId", "");
            if (!result) {
                System.out.println("✓ addLike with empty likedUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addLike with empty likedUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addLike with empty likedUserId threw exception: " + e.getMessage());
        }
    }

    // Test addPass method
    public void testAddPass() {
        System.out.println("Testing addPass...");
        testsTotal++;
        
        try {
            // Test with null userId
            boolean result = likeDAO.addPass(null, "passedUserId");
            if (!result) {
                System.out.println("✓ addPass with null userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addPass with null userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addPass with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null passedUserId
            boolean result = likeDAO.addPass("userId", null);
            if (!result) {
                System.out.println("✓ addPass with null passedUserId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addPass with null passedUserId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addPass with null passedUserId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            boolean result = likeDAO.addPass("", "passedUserId");
            if (!result) {
                System.out.println("✓ addPass with empty userId returns false");
                testsPassed++;
            } else {
                System.out.println("✗ addPass with empty userId should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ addPass with empty userId threw exception: " + e.getMessage());
        }
    }

    // Test getLikedUsers method
    public void testGetLikedUsers() {
        System.out.println("Testing getLikedUsers...");
        testsTotal++;
        
        try {
            // Test with null userId
            List<String> result = likeDAO.getLikedUsers(null);
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getLikedUsers with null userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getLikedUsers with null userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getLikedUsers with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            List<String> result = likeDAO.getLikedUsers("");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getLikedUsers with empty userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getLikedUsers with empty userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getLikedUsers with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent userId
            List<String> result = likeDAO.getLikedUsers("non-existent-user");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getLikedUsers with non-existent userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getLikedUsers with non-existent userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getLikedUsers with non-existent userId threw exception: " + e.getMessage());
        }
    }

    // Test getPassedUsers method
    public void testGetPassedUsers() {
        System.out.println("Testing getPassedUsers...");
        testsTotal++;
        
        try {
            // Test with null userId
            List<String> result = likeDAO.getPassedUsers(null);
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getPassedUsers with null userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getPassedUsers with null userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getPassedUsers with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            List<String> result = likeDAO.getPassedUsers("");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getPassedUsers with empty userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getPassedUsers with empty userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getPassedUsers with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent userId
            List<String> result = likeDAO.getPassedUsers("non-existent-user");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getPassedUsers with non-existent userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getPassedUsers with non-existent userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getPassedUsers with non-existent userId threw exception: " + e.getMessage());
        }
    }

    // Test isMatched method
    public void testIsMatched() {
        System.out.println("Testing isMatched...");
        testsTotal++;
        
        try {
            // Test with null userId1
            boolean result = likeDAO.isMatched(null, "userId2");
            if (!result) {
                System.out.println("✓ isMatched with null userId1 returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatched with null userId1 should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatched with null userId1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null userId2
            boolean result = likeDAO.isMatched("userId1", null);
            if (!result) {
                System.out.println("✓ isMatched with null userId2 returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatched with null userId2 should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatched with null userId2 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent users
            boolean result = likeDAO.isMatched("non-existent-user1", "non-existent-user2");
            if (!result) {
                System.out.println("✓ isMatched with non-existent users returns false");
                testsPassed++;
            } else {
                System.out.println("✗ isMatched with non-existent users should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ isMatched with non-existent users threw exception: " + e.getMessage());
        }
    }

    // Test getMatches method
    public void testGetMatches() {
        System.out.println("Testing getMatches...");
        testsTotal++;
        
        try {
            // Test with null userId
            List<String> result = likeDAO.getMatches(null);
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
            List<String> result = likeDAO.getMatches("");
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
            List<String> result = likeDAO.getMatches("non-existent-user");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getMatches with non-existent userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getMatches with non-existent userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getMatches with non-existent userId threw exception: " + e.getMessage());
        }
    }

    // Test getLikersOfUser method (instead of getUsersWhoLiked)
    public void testGetLikersOfUser() {
        System.out.println("Testing getLikersOfUser...");
        testsTotal++;
        
        try {
            // Test with null userId
            List<String> result = likeDAO.getLikersOfUser(null);
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getLikersOfUser with null userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getLikersOfUser with null userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getLikersOfUser with null userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty userId
            List<String> result = likeDAO.getLikersOfUser("");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getLikersOfUser with empty userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getLikersOfUser with empty userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getLikersOfUser with empty userId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent userId
            List<String> result = likeDAO.getLikersOfUser("non-existent-user");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getLikersOfUser with non-existent userId returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getLikersOfUser with non-existent userId should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getLikersOfUser with non-existent userId threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== LikeDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testAddLike();
        testAddPass();
        testGetLikedUsers();
        testGetPassedUsers();
        testIsMatched();
        testGetMatches();
        testGetLikersOfUser();
        
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
        LikeDAOTest test = new LikeDAOTest();
        test.runAllTests();
    }
} 