package heartsync;

import javax.swing.JButton;
import javax.swing.JPasswordField;

public class ShowHideController {
    private JPasswordField passwordField;
    private JButton toggleButton;
    private boolean isPasswordVisible;
    
    public ShowHideController(JPasswordField passwordField, JButton toggleButton) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;
        this.isPasswordVisible = false;
        
        toggleButton.addActionListener(e -> togglePasswordVisibility());
    }
    
    public void togglePasswordVisibility() {
        String currentPassword = String.valueOf(passwordField.getPassword());
        if (currentPassword.equals("Enter password")) {
            return;
        }
        
        isPasswordVisible = !isPasswordVisible;
        toggleButton.setText(isPasswordVisible ? "Hide" : "Show");
        
        if (isPasswordVisible) {
            passwordField.setEchoChar((char)0);
        } else {
            passwordField.setEchoChar('•');
        }
    }
    
    public String getActualPassword() {
        String pass = String.valueOf(passwordField.getPassword());
        return pass.equals("Enter password") ? "" : pass;
    }
    
    public void reset() {
        isPasswordVisible = false;
        toggleButton.setText("Show");
        passwordField.setText("Enter password");
        passwordField.setEchoChar((char)0);
        passwordField.setForeground(java.awt.Color.GRAY);
    }
} 