package TestPackages;

import heartsync.dao.ChatDAO;
import heartsync.model.Chat;
import java.util.List;

/**
 * Test class for ChatDAO
 * Tests all public methods with different scenarios
 */
public class ChatDAOTest {

    private ChatDAO chatDAO;
    private Chat testChat;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public ChatDAOTest() {
        chatDAO = new ChatDAO();
        setupTestData();
    }

    private void setupTestData() {
        testChat = new Chat();
        testChat.setSenderId("user1");
        testChat.setReceiverId("user2");
        testChat.setMessage("Hello, this is a test message");
        testChat.setTimestamp(System.currentTimeMillis());
        testChat.setMessageId("test-message-id");
    }

    // Test getChatId method
    public void testGetChatId() {
        System.out.println("Testing getChatId...");
        testsTotal++;
        
        try {
            // Test with null user1
            String result = chatDAO.getChatId(null, "user2");
            if (result != null) {
                System.out.println("✓ getChatId with null user1 returns non-null string");
                testsPassed++;
            } else {
                System.out.println("✗ getChatId with null user1 should return non-null string");
            }
        } catch (Exception e) {
            System.out.println("✗ getChatId with null user1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null user2
            String result = chatDAO.getChatId("user1", null);
            if (result != null) {
                System.out.println("✓ getChatId with null user2 returns non-null string");
                testsPassed++;
            } else {
                System.out.println("✗ getChatId with null user2 should return non-null string");
            }
        } catch (Exception e) {
            System.out.println("✗ getChatId with null user2 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty user1
            String result = chatDAO.getChatId("", "user2");
            if (result != null) {
                System.out.println("✓ getChatId with empty user1 returns non-null string");
                testsPassed++;
            } else {
                System.out.println("✗ getChatId with empty user1 should return non-null string");
            }
        } catch (Exception e) {
            System.out.println("✗ getChatId with empty user1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with valid users - should be consistent
            String result1 = chatDAO.getChatId("user1", "user2");
            String result2 = chatDAO.getChatId("user2", "user1");
            if (result1 != null && result2 != null && result1.equals(result2)) {
                System.out.println("✓ getChatId is consistent regardless of user order");
                testsPassed++;
            } else {
                System.out.println("✗ getChatId should be consistent regardless of user order");
            }
        } catch (Exception e) {
            System.out.println("✗ getChatId consistency test threw exception: " + e.getMessage());
        }
    }

    // Test sendMessage method
    public void testSendMessage() {
        System.out.println("Testing sendMessage...");
        testsTotal++;
        
        try {
            // Test with null chat
            chatDAO.sendMessage(null);
            System.out.println("✓ sendMessage with null chat doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ sendMessage with null chat threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with chat missing senderId
            Chat chatWithoutSender = new Chat();
            chatWithoutSender.setReceiverId("user2");
            chatWithoutSender.setMessage("test message");
            chatWithoutSender.setTimestamp(System.currentTimeMillis());
            
            chatDAO.sendMessage(chatWithoutSender);
            System.out.println("✓ sendMessage with chat missing senderId doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ sendMessage with chat missing senderId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with chat missing receiverId
            Chat chatWithoutReceiver = new Chat();
            chatWithoutReceiver.setSenderId("user1");
            chatWithoutReceiver.setMessage("test message");
            chatWithoutReceiver.setTimestamp(System.currentTimeMillis());
            
            chatDAO.sendMessage(chatWithoutReceiver);
            System.out.println("✓ sendMessage with chat missing receiverId doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ sendMessage with chat missing receiverId threw exception: " + e.getMessage());
        }
    }

    // Test getConversation method
    public void testGetConversation() {
        System.out.println("Testing getConversation...");
        testsTotal++;
        
        try {
            // Test with null user1
            List<Chat> result = chatDAO.getConversation(null, "user2");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getConversation with null user1 returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getConversation with null user1 should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getConversation with null user1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null user2
            List<Chat> result = chatDAO.getConversation("user1", null);
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getConversation with null user2 returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getConversation with null user2 should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getConversation with null user2 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty user1
            List<Chat> result = chatDAO.getConversation("", "user2");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getConversation with empty user1 returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getConversation with empty user1 should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getConversation with empty user1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent users
            List<Chat> result = chatDAO.getConversation("non-existent-user1", "non-existent-user2");
            if (result != null && result.isEmpty()) {
                System.out.println("✓ getConversation with non-existent users returns empty list");
                testsPassed++;
            } else {
                System.out.println("✗ getConversation with non-existent users should return empty list");
            }
        } catch (Exception e) {
            System.out.println("✗ getConversation with non-existent users threw exception: " + e.getMessage());
        }
    }

    // Test getLastMessage method
    public void testGetLastMessage() {
        System.out.println("Testing getLastMessage...");
        testsTotal++;
        
        try {
            // Test with null user1
            Chat result = chatDAO.getLastMessage(null, "user2");
            if (result == null) {
                System.out.println("✓ getLastMessage with null user1 returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastMessage with null user1 should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastMessage with null user1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with null user2
            Chat result = chatDAO.getLastMessage("user1", null);
            if (result == null) {
                System.out.println("✓ getLastMessage with null user2 returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastMessage with null user2 should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastMessage with null user2 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty user1
            Chat result = chatDAO.getLastMessage("", "user2");
            if (result == null) {
                System.out.println("✓ getLastMessage with empty user1 returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastMessage with empty user1 should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastMessage with empty user1 threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent users
            Chat result = chatDAO.getLastMessage("non-existent-user1", "non-existent-user2");
            if (result == null) {
                System.out.println("✓ getLastMessage with non-existent users returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastMessage with non-existent users should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastMessage with non-existent users threw exception: " + e.getMessage());
        }
    }

    // Test getLastChat method
    public void testGetLastChat() {
        System.out.println("Testing getLastChat...");
        testsTotal++;
        
        try {
            // Test with null chatId
            Chat result = chatDAO.getLastChat(null);
            if (result == null) {
                System.out.println("✓ getLastChat with null chatId returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastChat with null chatId should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastChat with null chatId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with empty chatId
            Chat result = chatDAO.getLastChat("");
            if (result == null) {
                System.out.println("✓ getLastChat with empty chatId returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastChat with empty chatId should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastChat with empty chatId threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with non-existent chatId
            Chat result = chatDAO.getLastChat("non-existent-chat-id");
            if (result == null) {
                System.out.println("✓ getLastChat with non-existent chatId returns null");
                testsPassed++;
            } else {
                System.out.println("✗ getLastChat with non-existent chatId should return null");
            }
        } catch (Exception e) {
            System.out.println("✗ getLastChat with non-existent chatId threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== ChatDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testGetChatId();
        testSendMessage();
        testGetConversation();
        testGetLastMessage();
        testGetLastChat();
        
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
        ChatDAOTest test = new ChatDAOTest();
        test.runAllTests();
    }
} 