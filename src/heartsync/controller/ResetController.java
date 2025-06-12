/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.dao.UserDAOForgot;
import heartsync.model.UserForgot;
import heartsync.view.ResetPassword;

/**
 *
 * @author manjil-basnet
 */
public class ResetController {
    private int userId;
    private UserDAOForgot userDAO;

    public ResetController() {
        userDAO = new UserDAOForgot();
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
            UserForgot user = userDAO.findByUsername(String.valueOf(userId));
            if (user != null) {
                userDAO.updatePassword(user.getUsername(), newPassword);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}