/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.controller;

import heartsync.dao.ResetPasswordDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manjil-basnet
 */
public class ResetController {
    private static final Logger LOGGER = Logger.getLogger(ResetController.class.getName());
    private ResetPasswordDAO resetPasswordDAO;

    public ResetController() {
        resetPasswordDAO = new ResetPasswordDAO();
    }

    public boolean validateUser(String username) {
        try {
            return resetPasswordDAO.validateUser(username);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating user: {0}", e.getMessage());
            return false;
        }
    }

    public boolean resetPassword(String username, String newPassword) {
        try {
            return resetPasswordDAO.resetPassword(username, newPassword);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error resetting password: {0}", e.getMessage());
            return false;
        }
    }
}