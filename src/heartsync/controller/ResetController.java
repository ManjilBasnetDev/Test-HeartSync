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
    private ResetDAO resetPasswordDAO;

    public ResetController() {
        resetPasswordDAO = new ResetDAO();
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
            User user = resetPasswordDAO.getUserById(userId);
            if (user != null) {
                user.setPassword(newPassword);
                return resetPasswordDAO.updateUser(user);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}