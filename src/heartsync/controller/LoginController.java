// LoginController.java - Controller for login functionality
package heartsync.controller;

import heartsync.model.User;
import heartsync.model.LoginModel;
import heartsync.model.UserProfile;
import heartsync.dao.UserDAO;
import heartsync.view.LoginView;
import heartsync.view.HomePage;
import heartsync.view.AdminDashboard;
import heartsync.view.ForgotPassword;
import heartsync.view.DatingApp;
import heartsync.navigation.WindowManager;
import java.awt.Cursor;
import javax.swing.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;

public class LoginController {
    private static LoginView currentLoginView = null;
    private static DatingApp currentDatingApp = null;
    private static HomePage homePageInstance = null;
    private final LoginView view;
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    
    /**
     * Gets the current login view instance if it exists
     * @return The current LoginView instance, or null if none exists
     */
    public static LoginView getCurrentLoginView() {
        return currentLoginView != null && currentLoginView.isDisplayable() ? currentLoginView : null;
    }
    
    public static void createAndShowLoginView() {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            currentLoginView = loginView;
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
    
    private final UserDAO userDAO;
    private final LoginModel loginModel;
    
    public LoginController(LoginView view) {
        this.view = view;
        this.loginModel = new LoginModel();
        this.userDAO = new UserDAO();
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
                
                // Close any existing HomePage instance
                HomePage homePage = HomePage.getInstance();
                if (homePage != null && homePage.isDisplayable()) {
                    homePage.dispose();
                }
                
                // If admin, open AdminDashboard, else open DatingApp
                if (user.getUserType() != null && user.getUserType().equalsIgnoreCase("admin")) {
                    WindowManager.show(AdminDashboard.class, AdminDashboard::new, null);
                } else {
                    // Create and show DatingApp with the authenticated user's username
                    DatingApp datingApp = new DatingApp(user.getUsername());
                    datingApp.setVisible(true);
                }
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error opening user view", e);
                JOptionPane.showMessageDialog(
                    null,
                    "Error opening application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    
    private void handleBack() {
        if (view != null) {
            view.dispose();
            WindowManager.show(HomePage.class, HomePage::new, null);
        }
    }
    
    private void handleForgotPassword() {
        if (view != null) {
            view.dispose();
            WindowManager.show(ForgotPassword.class, ForgotPassword::new, null);
        }
    }
}