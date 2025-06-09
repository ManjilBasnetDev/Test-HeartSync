/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heartsync;

import heartsync.view.HomePage;
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
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Launch the application on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                HomePage mainWindow = new HomePage();
                mainWindow.setLocationRelativeTo(null); // Center on screen
                mainWindow.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}