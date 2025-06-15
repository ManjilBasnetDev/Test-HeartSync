/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;


public class registercontrol {

    /**
     * Validates the user form fields (basic validation).
     */
    public boolean validateUserForm(String username, String password, String confirmPassword) {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        return password.equals(confirmPassword);
    }
}
