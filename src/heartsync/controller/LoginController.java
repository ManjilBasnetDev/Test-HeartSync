// LoginController.java - Controller for login functionality
package heartsync.controller;

import heartsync.view.LoginView;
import heartsync.dao.UserDAO;
import heartsync.model.User;  // Add this import
import heartsync.model.LoginModel;  // Add this import
import heartsync.view.Swipe;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginController {
    private static LoginView currentLoginView = null;
    private LoginView view;
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
        view.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        
        // Perform authentication in background thread
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
                view.setCursor(java.awt.Cursor.getDefaultCursor());
                
                try {
                    User authenticatedUser = get();
                    
                    if (authenticatedUser != null) {
                        // Successful login
                        view.showMessage(
                            "Welcome back, " + authenticatedUser.getUsername() + "!",
                            "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        // Store user session
                        // In a real app, you might want to store this in a session manager
                        
                        // Close login window and open main application window
                        view.dispose();
                        
                        // Open main application window in the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> {
                            Swipe swipeView = new Swipe();
                            // You might want to pass the authenticated user to the Swipe view
                            swipeView.setVisible(true);
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
    
    private void handleBack() {
        // Close the login view
        view.dispose();
        // Open the HomePage in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new heartsync.view.HomePage().setVisible(true);
        });
    }
    
    private void handleForgotPassword() {
        // Dispose current login view and open ForgotPassword window
        view.dispose();
        new heartsync.view.ForgotPassword().setVisible(true);
    }
    
    /**
     * Creates and shows the login view using the controller.
     * This is a static method that can be called from anywhere in the application
     * to show the login screen. Ensures only one login view exists at a time.
     */
    public static void createAndShowLoginView() {
        // Ensure UI updates happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // If a login view already exists, bring it to front and return
            if (currentLoginView != null && currentLoginView.isDisplayable()) {
                currentLoginView.toFront();
                currentLoginView.requestFocus();
                return;
            }
            
            // Create new login view
            currentLoginView = new LoginView();
            LoginController controller = new LoginController(currentLoginView);
            
            currentLoginView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            currentLoginView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    currentLoginView = null;
                }
            });
            
            // Center and show the view
            currentLoginView.setLocationRelativeTo(null);
            currentLoginView.setVisible(true);
        });
    }
}