package heartsync;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.SwingUtilities;

public class ShowHideController {
    private final JPasswordField passwordField;
    private final JButton toggleButton;
    private boolean isPasswordVisible;
    
    public ShowHideController(JPasswordField passwordField, JButton toggleButton) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;
        this.isPasswordVisible = false;
        
        toggleButton.addActionListener(e -> togglePasswordVisibility());
        setupInitialState();
    }
    
    private void setupInitialState() {
        passwordField.setText("");
        passwordField.setEchoChar('•');
        passwordField.setForeground(Color.BLACK);
        toggleButton.setText("Show");
    }
    
    public void togglePasswordVisibility() {
        String pass = String.valueOf(passwordField.getPassword());
        if (pass.isEmpty()) {
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
        return String.valueOf(passwordField.getPassword());
    }
    
    public void reset() {
        isPasswordVisible = false;
        setupInitialState();
    }
    
    public void onFocusGained() {
        SwingUtilities.invokeLater(() -> {
            passwordField.setForeground(Color.BLACK);
            if (!isPasswordVisible) {
                passwordField.setEchoChar('•');
            }
        });
    }
    
    public void onFocusLost() {
        SwingUtilities.invokeLater(() -> {
            String pass = String.valueOf(passwordField.getPassword());
            if (pass.isEmpty()) {
                reset();
            }
        });
    }
} 