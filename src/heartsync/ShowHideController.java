package heartsync;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Color;

public class ShowHideController {
    private final JPasswordField passwordField;
    private final JButton toggleButton;
    private boolean isPasswordVisible;
    private static final String PLACEHOLDER = "Enter password";
    
    public ShowHideController(JPasswordField passwordField, JButton toggleButton) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;
        this.isPasswordVisible = false;
        
        toggleButton.addActionListener(e -> togglePasswordVisibility());
    }
    
    public void togglePasswordVisibility() {
        String pass = String.valueOf(passwordField.getPassword());
        if (pass.equals(PLACEHOLDER)) {
            return;
        }
        
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordField.setEchoChar((char) 0);
            toggleButton.setText("Hide");
        } else {
            passwordField.setEchoChar('•');
            toggleButton.setText("Show");
        }
    }
    
    public String getActualPassword() {
        String pass = String.valueOf(passwordField.getPassword());
        return pass.equals(PLACEHOLDER) ? "" : pass;
    }
    
    public void reset() {
        isPasswordVisible = false;
        toggleButton.setText("Show");
        passwordField.setText(PLACEHOLDER);
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.GRAY);
    }
    
    public void onFocusGained() {
        String pass = String.valueOf(passwordField.getPassword());
        if (pass.equals(PLACEHOLDER)) {
            passwordField.setText("");
            passwordField.setForeground(Color.BLACK);
            passwordField.setEchoChar('•');
        }
    }
    
    public void onFocusLost() {
        String pass = String.valueOf(passwordField.getPassword());
        if (pass.isEmpty() || pass.trim().isEmpty()) {
            reset();
        }
    }
} 