package TestPackages;

/**
 * Comprehensive Test Runner for all DAO classes
 * This class runs all individual DAO test suites and provides a summary
 */
public class AllDAOTestRunner {

    public static void main(String[] args) {
        System.out.println("===================================================");
        System.out.println("           HeartSync DAO Test Suite Runner         ");
        System.out.println("===================================================");
        System.out.println("Running comprehensive tests for all DAO classes...\n");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Run UserDAO tests
            System.out.println("1. Running UserDAO Tests...");
            UserDAOTest userDAOTest = new UserDAOTest();
            userDAOTest.runAllTests();
            System.out.println();
            
            // Run UserRegisterDAO tests
            System.out.println("2. Running UserRegisterDAO Tests...");
            UserRegisterDAOTest userRegisterDAOTest = new UserRegisterDAOTest();
            userRegisterDAOTest.runAllTests();
            System.out.println();
            
            // Run LikeDAO tests
            System.out.println("3. Running LikeDAO Tests...");
            LikeDAOTest likeDAOTest = new LikeDAOTest();
            likeDAOTest.runAllTests();
            System.out.println();
            
            // Run ChatDAO tests
            System.out.println("4. Running ChatDAO Tests...");
            ChatDAOTest chatDAOTest = new ChatDAOTest();
            chatDAOTest.runAllTests();
            System.out.println();
            
            // Run MatchDAO tests
            System.out.println("5. Running MatchDAO Tests...");
            MatchDAOTest matchDAOTest = new MatchDAOTest();
            matchDAOTest.runAllTests();
            System.out.println();
            
            // Run ResetDAO tests
            System.out.println("6. Running ResetDAO Tests...");
            ResetDAOTest resetDAOTest = new ResetDAOTest();
            resetDAOTest.runAllTests();
            System.out.println();
            
            // Run ContactDAO tests
            System.out.println("7. Running ContactDAO Tests...");
            ContactDAOTest contactDAOTest = new ContactDAOTest();
            contactDAOTest.runAllTests();
            System.out.println();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.println("===================================================");
            System.out.println("              Test Suite Summary                    ");
            System.out.println("===================================================");
            System.out.println("✓ All DAO test suites completed successfully!");
            System.out.println("Total execution time: " + duration + "ms");
            System.out.println("===================================================");
            
        } catch (Exception e) {
            System.err.println("Error running test suite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method to run individual test for a specific DAO
     * Usage: AllDAOTestRunner.runSpecificTest("UserDAO");
     */
    public static void runSpecificTest(String daoName) {
        System.out.println("Running tests for: " + daoName);
        
        try {
            switch (daoName.toLowerCase()) {
                case "userdao":
                    new UserDAOTest().runAllTests();
                    break;
                case "userregisterdao":
                    new UserRegisterDAOTest().runAllTests();
                    break;
                case "likedao":
                    new LikeDAOTest().runAllTests();
                    break;
                case "chatdao":
                    new ChatDAOTest().runAllTests();
                    break;
                case "matchdao":
                    new MatchDAOTest().runAllTests();
                    break;
                case "resetdao":
                    new ResetDAOTest().runAllTests();
                    break;
                case "contactdao":
                    new ContactDAOTest().runAllTests();
                    break;
                default:
                    System.out.println("Unknown DAO: " + daoName);
                    System.out.println("Available DAOs: UserDAO, UserRegisterDAO, LikeDAO, ChatDAO, MatchDAO, ResetDAO, ContactDAO");
            }
        } catch (Exception e) {
            System.err.println("Error running " + daoName + " tests: " + e.getMessage());
        }
    }

    /**
     * Method to display available test options
     */
    public static void showHelp() {
        System.out.println("HeartSync DAO Test Runner - Usage Options:");
        System.out.println("1. Run all tests: java AllDAOTestRunner");
        System.out.println("2. Run specific DAO test: AllDAOTestRunner.runSpecificTest(\"UserDAO\")");
        System.out.println("\nAvailable DAO Tests:");
        System.out.println("- UserDAO: Tests user authentication, creation, and management");
        System.out.println("- UserRegisterDAO: Tests user registration and validation");
        System.out.println("- LikeDAO: Tests like/pass functionality and matching");
        System.out.println("- ChatDAO: Tests chat message handling");
        System.out.println("- MatchDAO: Tests match creation and retrieval");
        System.out.println("- ResetDAO: Tests password reset functionality");
        System.out.println("- ContactDAO: Tests contact form handling");
    }
} 