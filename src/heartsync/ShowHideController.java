package heartsync;

import javax.swing.JButton;
import javax.swing.JPasswordField;

public class ShowHideController {
    private JPasswordField passwordField;
    private JButton toggleButton;
    private String actualPassword;
    private boolean isPasswordVisible;
    
    public ShowHideController(JPasswordField passwordField, JButton toggleButton) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;
        this.actualPassword = "";
        this.isPasswordVisible = false;
        
        toggleButton.addActionListener(e -> togglePasswordVisibility());
    }
    
    public void togglePasswordVisibility() {
        if (String.valueOf(passwordField.getPassword()).equals("Enter password") && actualPassword.isEmpty()) {
            return; // Don't toggle if it's just a placeholder
        }
        isPasswordVisible = !isPasswordVisible;
        toggleButton.setText(isPasswordVisible ? "Hide" : "Show");
        updatePasswordDisplay();
    }
    
    public void setActualPassword(String password) {
        this.actualPassword = password;
        updatePasswordDisplay();
    }
    
    public String getActualPassword() {
        return actualPassword;
    }
    
    public void reset() {
        actualPassword = "";
        isPasswordVisible = false;
        toggleButton.setText("Show");
        passwordField.setText("Enter password");
        passwordField.setEchoChar((char)0);
    }
    
    private void updatePasswordDisplay() {
        if (isPasswordVisible) {
            passwordField.setEchoChar((char)0);
        } else {
            passwordField.setEchoChar('•');
        }
    }
} 