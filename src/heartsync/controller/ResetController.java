/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.dao.UserDAOForgot;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ResetController {
    private int userId;
    private UserDAOForgot userDAOForgot;
    private String username;

    public ResetController() {
        this.userDAOForgot = new UserDAOForgot();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            // Validate inputs
            if (username == null || username.trim().isEmpty()) {
                throw new SQLException("Username not set for password reset");
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new SQLException("New password cannot be empty");
            }
            
            // Debug output
            System.out.println("Updating password for user: " + username);
            
            // Update the password using UserDAOForgot which handles Firebase correctly
            boolean success = userDAOForgot.updatePassword(username, newPassword);
            
            if (success) {
                System.out.println("Password updated successfully for user: " + username);
            } else {
                System.out.println("Failed to update password for user: " + username);
            }
            
            return success;
        } catch (SQLException e) {
            System.err.println("Error in updatePassword: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error updating password: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error in updatePassword: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Unexpected error updating password: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}