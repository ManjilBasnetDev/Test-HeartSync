/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.dao.ResetDAO;
import heartsync.model.User;
import heartsync.view.ResetPassword;
import javax.swing.JOptionPane;

/**
 * Controller class for handling password reset functionality
 * @author manjil-basnet
 */
public class ResetController {
    private int userId;
    private final ResetDAO resetDAO;

    public ResetController() {
        resetDAO = new ResetDAO();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void showResetView() {
        ResetPassword resetView = new ResetPassword(userId);
        resetView.setVisible(true);
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            if (newPassword == null || newPassword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Password cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            User user = resetDAO.getUserById(userId);
            if (user != null) {
                user.setPassword(newPassword);
                boolean success = resetDAO.updateUser(user);
                
                if (success) {
                    JOptionPane.showMessageDialog(null,
                        "Password updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Failed to update password",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                return success;
            } else {
                JOptionPane.showMessageDialog(null,
                    "User not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error updating password: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}