/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;


public class RegisterController {

    /**
     * Validates the user form fields (basic validation).
     * @param username the username to validate
     * @param password the password to validate
     * @param confirmPassword the confirmation password to check
     * @return true if validation passes, false otherwise
     */
    public boolean validateUserForm(String username, String password, String confirmPassword) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    /**
     * Validates a single username field.
     * @param username the username to validate
     * @return true if valid, false otherwise
     */
    public boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (username.length() < 3) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");
    }
}
