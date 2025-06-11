package heartsync;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ShowHideController {
    private JTextArea passwordField;
    private JButton toggleButton;
    private String actualPassword;
    private boolean isPasswordVisible;
    
    public ShowHideController(JTextArea passwordField, JButton toggleButton) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;
        this.actualPassword = "";
        this.isPasswordVisible = false;
        
        toggleButton.addActionListener(e -> togglePasswordVisibility());
    }
    
    public void togglePasswordVisibility() {
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
        passwordField.setText("");
    }
    
    private void updatePasswordDisplay() {
        if (isPasswordVisible) {
            passwordField.setText(actualPassword);
        } else {
            StringBuilder masked = new StringBuilder();
            for (int i = 0; i < actualPassword.length(); i++) {
                masked.append('•');
            }
            passwordField.setText(masked.toString());
        }
    }
} 