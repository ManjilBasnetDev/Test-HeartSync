package heartsync.controller;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ShowHideController {
    private final JTextArea passwordField;
    private final JButton toggleButton;
    private String actualPassword = "";
    private boolean isPasswordVisible = false;

    public ShowHideController(JTextArea passwordField, JButton toggleButton) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;
    }

    public void setActualPassword(String password) {
        this.actualPassword = password;
        updatePasswordField();
    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void toggleVisibility() {
        isPasswordVisible = !isPasswordVisible;
        toggleButton.setText(isPasswordVisible ? "Hide" : "Show");
        updatePasswordField();
    }

    public void reset() {
        actualPassword = "";
        isPasswordVisible = false;
        toggleButton.setText("Show");
        passwordField.setText("");
    }

    private void updatePasswordField() {
        if (isPasswordVisible) {
            passwordField.setText(actualPassword);
        } else {
            StringBuilder hidden = new StringBuilder();
            for (int i = 0; i < actualPassword.length(); i++) {
                hidden.append("•");
            }
            passwordField.setText(hidden.toString());
        }
        passwordField.setCaretPosition(passwordField.getText().length());
    }
}
