// LoginController.java - Controller for login functionality
package heartsync.controller;

import heartsync.model.User;
import heartsync.model.LoginModel;
import heartsync.model.UserProfile;
import heartsync.dao.UserDAO;
import heartsync.view.*;
import heartsync.navigation.WindowManager;
import java.awt.Cursor;
import javax.swing.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class LoginController {
    private static LoginView currentLoginView = null;
    private final LoginView view;
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    
    /**
     * Gets the current login view instance if it exists
     * @return The current LoginView instance, or null if none exists
     */
    public static LoginView getCurrentLoginView() {
        return currentLoginView != null && currentLoginView.isDisplayable() ? currentLoginView : null;
    }
    private final UserDAO userDAO;
    private final LoginModel loginModel;
    
    public LoginController(LoginView view) {
        this.view = view;
        this.loginModel = new LoginModel();
        
        // Initialize UserDAO
        this.userDAO = new UserDAO();
        
        // Set this controller in the view
        view.setController(this);
        
        initializeEventListeners();
    }
    
    private void initializeEventListeners() {
        // Login button listener
        view.addLoginButtonListener(e -> handleLogin());
        
        // Back button listener
        view.addBackButtonListener(e -> handleBack());
        
        // Show/Hide password button listener
        view.addShowPasswordButtonListener(e -> view.togglePasswordVisibility());
        
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
        String username = view.getUsername() != null ? view.getUsername().trim() : "";
        String password = view.getPassword() != null ? view.getPassword().trim() : "";
        
        // Set values in model
        loginModel.setUsername(username);
        loginModel.setPassword(password);
        
        // Validate using model
        String validationError = loginModel.validate();
        if (validationError != null) {
            view.showMessage(validationError, "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Disable login button and show loading state
        view.setLoginButtonEnabled(false);
        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Create a worker thread for authentication
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            private String username = view.getUsername();
            private String password = view.getPassword();
            
            @Override
            protected User doInBackground() throws Exception {
                try {
                    // Perform authentication in background
                    // Ensure DAO gets trimmed credentials
                    return userDAO.authenticateUser(username.trim(), password);
                } catch (Exception e) {
                    throw new Exception("Authentication failed: " + e.getMessage());
                }
            }
            
            @Override
            protected void done() {
                // Re-enable login button and reset cursor
                view.setLoginButtonEnabled(true);
                view.setCursor(Cursor.getDefaultCursor());
                
                try {
                    User authenticatedUser = get();
                    
                    if (authenticatedUser != null) {
                        // Successful login - show welcome message
                        view.showMessage(
                            "Welcome back, " + authenticatedUser.getUsername() + "!",
                            "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        // Close login window on successful login
                        view.dispose();
                        
                        // Open appropriate view based on user type
                        openUserView(authenticatedUser);
                        
                    } else {
                        // Authentication failed
                        view.showMessage(
                            "Invalid username or password. Please try again.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                        view.clearFields();
                    }
                } catch (java.util.concurrent.ExecutionException e) {
                    // Handle execution exceptions
                    Throwable cause = e.getCause();
                    view.showMessage(
                        "Login error: " + (cause != null ? cause.getMessage() : "Unknown error"),
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    // Handle other exceptions
                    view.showMessage(
                        "An error occurred: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        
        // Execute the worker
        worker.execute();
    }
    
    /**
     * Helper method to open the appropriate view based on user type
     */
    private void openUserView(User user) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the current user in both models
                User.setCurrentUser(user);
                UserProfile.setCurrentUser(null); // This will force reload from DB on next access
                
                // Close the login window
                if (view != null) {
                    view.dispose();
                }
                
                // Check user type and open appropriate view
                String userType = user.getUserType();
                if (userType != null && userType.equalsIgnoreCase("admin")) {
                    // Show admin dashboard for admin users
                    AdminDashboard.showAdminDashboard();
                } else {
                    // Show the Swipe view for regular users
                    WindowManager.show(Swipe.class, () -> new Swipe(), null);
                }
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error opening user view", e);
                if (view != null) {
                    view.showMessage("Error initializing application view: " + e.getMessage(),
                                   "Initialization Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private static HomePage homePageInstance = null;
    
    public void handleBack() {
        view.dispose();
        // Restore the HomePage if it exists
        if (homePageInstance != null && homePageInstance.isDisplayable()) {
            homePageInstance.setState(JFrame.NORMAL);
            homePageInstance.toFront();
        }
    }
    
    public void handleForgotPassword() {
        // Hide current login view instead of disposing it
        view.setVisible(false);
        
        // Show forgot password dialog and indicate we're coming from login
        try {
            heartsync.view.ForgotPassword.showForgotPassword(true);
        } catch (Exception e) {
            System.err.println("Error showing forgot password: " + e.getMessage());
            e.printStackTrace();
            // If there's an error, show the login view again
            view.setVisible(true);
        }
    }
    
    /**
     * Creates and shows the login view using the controller.
     * This is a static method that can be called from anywhere in the application
     * to show the login screen. Ensures only one login view exists at a time.
     */
    public static void createAndShowLoginView() {
        // Ensure UI updates happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Store reference to the HomePage if it exists
            homePageInstance = WindowManager.getWindow(HomePage.class, () -> new HomePage(null));

            // If a login view already exists and is visible, bring it to front and return
            if (currentLoginView != null) {
                if (currentLoginView.isDisplayable()) {
                    currentLoginView.setExtendedState(JFrame.NORMAL);
                    currentLoginView.setVisible(true);
                    return;
                } else {
                    // Clean up any disposed but not null reference
                    currentLoginView = null;
                }
            }
            
            try {
                // Create new login view and controller
                currentLoginView = new LoginView();
                new LoginController(currentLoginView);
                
                // Add window listener to clean up reference when closed
                currentLoginView.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        // Clean up the reference when window is closed
                        currentLoginView = null;
                        // Restore the HomePage if it exists
                        if (homePageInstance != null && homePageInstance.isDisplayable()) {
                            homePageInstance.setState(JFrame.NORMAL);
                            homePageInstance.toFront();
                        }
                    }
                    
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        // Ensure cleanup happens even if window is force-closed
                        currentLoginView = null;
                        // Restore the HomePage if it exists
                        if (homePageInstance != null && homePageInstance.isDisplayable()) {
                            homePageInstance.setState(JFrame.NORMAL);
                            homePageInstance.toFront();
                        }
                    }
                });
                
                // Center and show the view
                currentLoginView.setLocationRelativeTo(null);
                currentLoginView.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error creating login view: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}