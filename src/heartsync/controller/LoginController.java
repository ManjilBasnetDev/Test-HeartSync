// LoginController.java - Controller for login functionality
package heartsync.controller;

import heartsync.view.LoginView;
import heartsync.dao.UserDAO;
import heartsync.model.User;
import heartsync.model.LoginModel;
import heartsync.view.Swipe;
import heartsync.view.HomePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginController {
    private static LoginView currentLoginView = null;
    private LoginView view;
    
    /**
     * Gets the current login view instance if it exists
     * @return The current LoginView instance, or null if none exists
     */
    public static LoginView getCurrentLoginView() {
        return currentLoginView != null && currentLoginView.isDisplayable() ? currentLoginView : null;
    }
    private UserDAO userDAO;
    private LoginModel loginModel;
    
    public LoginController(LoginView view) {
        this.view = view;
        this.loginModel = new LoginModel();
        
        // Initialize UserDAO with proper error handling
        try {
            this.userDAO = new UserDAO();
        } catch (Exception e) {
            System.err.println("Failed to initialize UserDAO: " + e.getMessage());
            e.printStackTrace();
            // Show error to user
            JOptionPane.showMessageDialog(view, 
                "Database connection failed. Please check your database configuration.", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        initializeEventListeners();
    }
    
    private void initializeEventListeners() {
        // Login button listener
        view.addLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Back button listener
        view.addBackButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });
        
        // Show/Hide password button listener
        view.addShowPasswordButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.togglePasswordVisibility();
            }
        });
        
        // Forgot password link listener
        view.addForgotPasswordListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleForgotPassword();
            }
        });
        
        // Username field focus listener for placeholder text
        view.addUsernameFieldFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField) e.getSource();
                if (field.getText().equals("USERNAME")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                JTextField field = (JTextField) e.getSource();
                if (field.getText().trim().isEmpty()) {
                    field.setText("USERNAME");
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    private static Swipe currentSwipeView = null;
    
    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();
        
        // Set values in model
        loginModel.setUsername(username);
        loginModel.setPassword(password);
        
        // Validate using model
        String validationError = loginModel.validate();
        if (validationError != null) {
            view.showMessage(validationError, "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if UserDAO is initialized
        if (userDAO == null) {
            view.showMessage("Database connection not available. Please restart the application.", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Show loading cursor
        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Use SwingWorker for database operations to prevent UI freezing
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                // Simulate network delay (remove in production)
                Thread.sleep(500);
                return userDAO.authenticateUser(username, password);
            }
            
            @Override
            protected void done() {
                // Reset cursor
                view.setCursor(Cursor.getDefaultCursor());
                
                try {
                    User authenticatedUser = get();
                    
                    if (authenticatedUser != null) {
                        // Successful login
                        view.showMessage(
                            "Welcome back, " + authenticatedUser.getUsername() + "!",
                            "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        // Close login window
                        view.dispose();
                        
                        // Show existing Swipe view or create a new one
                        SwingUtilities.invokeLater(() -> {
                            if (currentSwipeView == null || !currentSwipeView.isDisplayable()) {
                                currentSwipeView = new Swipe();
                                currentSwipeView.setUser(authenticatedUser);
                                currentSwipeView.setVisible(true);
                            } else {
                                currentSwipeView.setExtendedState(JFrame.NORMAL);
                                currentSwipeView.toFront();
                                currentSwipeView.requestFocus();
                            }
                        });
                        
                    } else {
                        // Authentication failed
                        view.showMessage(
                            "Invalid username or password. Please try again.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                        view.clearFields();
                    }
                } catch (Exception e) {
                    view.showMessage(
                        "An error occurred during login: " + e.getMessage(),
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    private static HomePage homePageInstance = null;
    
    private void handleBack() {
        // Close the login view
        view.dispose();
        // Show existing HomePage or create a new one if it doesn't exist
        SwingUtilities.invokeLater(() -> {
            if (homePageInstance == null || !homePageInstance.isDisplayable()) {
                homePageInstance = new heartsync.view.HomePage();
                homePageInstance.setVisible(true);
            } else {
                homePageInstance.setExtendedState(JFrame.NORMAL);
                homePageInstance.toFront();
                homePageInstance.requestFocus();
            }
        });
    }
    
    private void handleForgotPassword() {
        // Dispose current login view and open ForgotPassword window using singleton
        view.dispose();
        heartsync.view.ForgotPassword.showForgotPassword();
    }
    
    /**
     * Creates and shows the login view using the controller.
     * This is a static method that can be called from anywhere in the application
     * to show the login screen. Ensures only one login view exists at a time.
     */
    public static void createAndShowLoginView() {
        // Ensure UI updates happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // If a login view already exists and is visible, bring it to front and return
            if (currentLoginView != null) {
                if (currentLoginView.isDisplayable()) {
                    currentLoginView.setExtendedState(JFrame.NORMAL);
                    currentLoginView.toFront();
                    currentLoginView.requestFocus();
                    return;
                } else {
                    // Clean up any disposed but not null reference
                    currentLoginView = null;
                }
            }
            
            // Create new login view and controller
            currentLoginView = new LoginView();
            new LoginController(currentLoginView);
            
            // Add window listener to clean up reference when closed
            currentLoginView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // Clean up the reference when window is closed
                    currentLoginView = null;
                }
                
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    // Ensure cleanup happens even if window is force-closed
                    currentLoginView = null;
                }
            });
            
            // Center and show the view
            currentLoginView.setLocationRelativeTo(null);
            currentLoginView.setVisible(true);
        });
    }
}