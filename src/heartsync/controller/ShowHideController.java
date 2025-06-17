/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author manjil-basnet
 */
public class ShowHideController {
    private final JTextArea passwordField;
    private final JButton showPasswordButton;
    private String actualPassword = "";
    private boolean isPasswordVisible = false;
    private boolean isUpdating = false;
    private static final String PLACEHOLDER = "Enter password";

    public ShowHideController(JTextArea passwordField, JButton showPasswordButton) {
        if (passwordField == null || showPasswordButton == null) {
            throw new IllegalArgumentException("Password field and show button cannot be null");
        }
        this.passwordField = passwordField;
        this.showPasswordButton = showPasswordButton;
        initialize();
    }

    private void initialize() {
        showPasswordButton.setText("Show");
        
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdating) {
                    SwingUtilities.invokeLater(() -> updatePassword());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdating) {
                    SwingUtilities.invokeLater(() -> updatePassword());
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
        });

        showPasswordButton.addActionListener(e -> {
            String currentText = passwordField.getText();
            if (!currentText.equals(PLACEHOLDER)) {
                togglePasswordVisibility();
            }
        });
    }

    private void updatePassword() {
        String currentText = passwordField.getText();
        
        // Ignore if it's the placeholder text
        if (currentText.equals(PLACEHOLDER)) {
            return;
        }

        // Only update actual password if the text isn't masked
        if (!currentText.matches("^[•]+$")) {
            actualPassword = currentText;
            // If we're in hidden mode, mask the password
            if (!isPasswordVisible) {
                maskCurrentPassword();
            }
        }
    }

    private void togglePasswordVisibility() {
        try {
            // Store current caret position
            int caretPosition = passwordField.getCaretPosition();
            
            isPasswordVisible = !isPasswordVisible;
            showPasswordButton.setText(isPasswordVisible ? "Hide" : "Show");

            isUpdating = true;
            if (isPasswordVisible) {
                // Show actual password
                passwordField.setText(actualPassword);
            } else {
                // Mask the password
                passwordField.setText(maskPassword(actualPassword));
            }
            
            // Restore caret position if it's valid
            if (caretPosition <= passwordField.getText().length()) {
                passwordField.setCaretPosition(caretPosition);
            }
        } finally {
            isUpdating = false;
        }
    }

    private void maskCurrentPassword() {
        if (!actualPassword.isEmpty()) {
            isUpdating = true;
            try {
                passwordField.setText(maskPassword(actualPassword));
            } finally {
                isUpdating = false;
            }
        }
    }

    private String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            masked.append("•");
        }
        return masked.toString();
    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void setActualPassword(String password) {
        if (password == null || password.equals(PLACEHOLDER)) {
            actualPassword = "";
            return;
        }
        
        actualPassword = password;
        isUpdating = true;
        try {
            if (isPasswordVisible) {
                passwordField.setText(actualPassword);
            } else {
                passwordField.setText(maskPassword(actualPassword));
            }
        } finally {
            isUpdating = false;
        }
    }

    public void reset() {
        isUpdating = true;
        try {
            actualPassword = "";
            isPasswordVisible = false;
            showPasswordButton.setText("Show");
            passwordField.setText("");
        } finally {
            isUpdating = false;
        }
    }
    
    public boolean isPasswordVisible() {
        return isPasswordVisible;
    }
}
