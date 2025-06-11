/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.dao.UserDAO;
import heartsync.model.User;
import heartsync.view.ResetPassword;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manjil-basnet
 */
public class ResetController {
    private static final Logger LOGGER = Logger.getLogger(ResetController.class.getName());
    private int userId;
    private final UserDAO userDAO;

    public ResetController() {
        try {
            userDAO = new UserDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize UserDAO", e);
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void showResetView() {
        ResetPassword resetView = new ResetPassword(userId);
        resetView.setVisible(true);
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            // Input validation
            if (newPassword == null || newPassword.trim().isEmpty()) {
                LOGGER.warning("Attempt to update password with empty value");
                return false;
            }

            User user = userDAO.getUserById(userId);
            if (user != null) {
                user.setPassword(newPassword);
                return userDAO.updateUser(user);
            }
            LOGGER.warning("No user found with ID: " + userId);
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while updating password", e);
            return false;
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while updating password", e);
            return false;
        }
    }

    public boolean verifySecurityAnswers(String username, String favoriteColor, String firstSchool) {
        try {
            return userDAO.verifySecurityQuestions(username, favoriteColor, firstSchool);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error verifying security answers", e);
            return false;
        }
    }

    public User getUserByUsername(String username) {
        try {
            return userDAO.getUserByUsername(username);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username", e);
            return null;
        }
    }
}