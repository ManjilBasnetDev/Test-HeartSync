/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.dao.ResetDAO;
import heartsync.model.User;
import heartsync.view.ResetPassword;

/**
 *
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

    public void showResetView() {
        ResetPassword resetView = new ResetPassword(userId);
        resetView.setVisible(true);
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            // Input validation
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            User user = resetDAO.getUserById(userId);
            if (user != null) {
                user.setPassword(newPassword);
                return resetDAO.updateUser(user);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifySecurityAnswers(String username, String favoriteColor, String firstSchool) {
        try {
            User user = resetDAO.getUserByUsername(username);
            if (user != null) {
                return favoriteColor.equals(user.getFavoriteColor())
                        && firstSchool.equals(user.getFirstSchool());
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        return resetDAO.getUserByUsername(username);
    }
}