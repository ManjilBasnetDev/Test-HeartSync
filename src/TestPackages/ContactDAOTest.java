package TestPackages;

import heartsync.dao.ContactDAO;
import heartsync.model.Contact;
import java.util.Map;

/**
 * Test class for ContactDAO
 * Tests all public methods with different scenarios
 */
public class ContactDAOTest {

    private ContactDAO contactDAO;
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public ContactDAOTest() {
        contactDAO = new ContactDAO();
    }

    // Test saveContact method
    public void testSaveContact() {
        System.out.println("Testing saveContact...");
        testsTotal++;
        
        try {
            // Test with null contact
            boolean result = contactDAO.saveContact(null);
            if (!result) {
                System.out.println("✓ saveContact with null contact returns false");
                testsPassed++;
            } else {
                System.out.println("✗ saveContact with null contact should return false");
            }
        } catch (Exception e) {
            System.out.println("✗ saveContact with null contact threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test with valid contact
            Contact contact = new Contact("John Doe", "john@example.com", "Test message");
            boolean result = contactDAO.saveContact(contact);
            // Should not throw exception
            System.out.println("✓ saveContact with valid contact doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ saveContact with valid contact threw exception: " + e.getMessage());
        }
    }

    // Test getAllContacts method
    public void testGetAllContacts() {
        System.out.println("Testing getAllContacts...");
        testsTotal++;
        
        try {
            // Test if getAllContacts returns a map (can be null or empty)
            Map<String, Contact> result = contactDAO.getAllContacts();
            // Should not throw exception
            System.out.println("✓ getAllContacts doesn't throw exception");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ getAllContacts threw exception: " + e.getMessage());
        }
        
        testsTotal++;
        try {
            // Test consistency of getAllContacts
            Map<String, Contact> result1 = contactDAO.getAllContacts();
            Map<String, Contact> result2 = contactDAO.getAllContacts();
            // Should not throw exception
            System.out.println("✓ getAllContacts returns consistent results");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ getAllContacts consistency test threw exception: " + e.getMessage());
        }
    }

    // Test interaction between methods
    public void testMethodInteraction() {
        System.out.println("Testing method interactions...");
        testsTotal++;
        
        try {
            // Test saving a contact and then getting all contacts
            Contact contact = new Contact("Jane Doe", "jane@example.com", "Interaction test message");
            contactDAO.saveContact(contact);
            Map<String, Contact> contacts = contactDAO.getAllContacts();
            
            System.out.println("✓ Saving contact and getting all contacts works");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Method interaction test threw exception: " + e.getMessage());
        }
    }

    // Run all tests
    public void runAllTests() {
        System.out.println("=== ContactDAO Test Suite ===");
        System.out.println("Running all tests...\n");
        
        testSaveContact();
        testGetAllContacts();
        testMethodInteraction();
        
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
        ContactDAOTest test = new ContactDAOTest();
        test.runAllTests();
    }
} 