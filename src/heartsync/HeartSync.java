/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heartsync;

import heartsync.view.HomePage;
import heartsync.database.TestUserCleanup;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for the HeartSync Dating Application.
 * This class initializes the main window and sets up the application environment.
 * 
 * @author manjil-basnet
 */
public class HeartSync {

    /**
     * Main method to start the application.
     * Sets up the look and feel, and launches the main window.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Clean up all test/demo users on application startup
            System.out.println("HeartSync Dating Application Starting...");
            TestUserCleanup.runSilentCleanup();
            
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set button UI defaults
            UIManager.put("Button.background", new java.awt.Color(70, 130, 180));
            UIManager.put("Button.foreground", java.awt.Color.WHITE);
            UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            
            // Set panel UI defaults
            UIManager.put("Panel.background", new java.awt.Color(255, 240, 245));
            UIManager.put("Label.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            UIManager.put("TextField.font", new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            UIManager.put("TextArea.font", new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            
            // Launch the HomePage directly
            SwingUtilities.invokeLater(() -> {
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}