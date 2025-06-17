package heartsync.controller;

import heartsync.dao.UserDAOForgot;
import heartsync.view.SecurityQuestionsView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SecurityQuestionsController {
    private final SecurityQuestionsView view;
    private final String username; // we identify row by username (already unique)

    public SecurityQuestionsController(String username) {
        this.username = username;
        this.view = new SecurityQuestionsView(null);
        init();
    }

    private void init() {
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }
        };
        view.getFavoriteColorField().getDocument().addDocumentListener(dl);
        view.getFirstSchoolField().getDocument().addDocumentListener(dl);

        view.getSaveButton().addActionListener(e -> save());
    }

    private void validate() {
        String color = view.getFavoriteColorField().getText().trim();
        String school = view.getFirstSchoolField().getText().trim();
        boolean ok = color.length() >= 3 && school.length() >= 3;
        view.getSaveButton().setEnabled(ok);
    }

    private void save() {
        String color = view.getFavoriteColorField().getText().trim();
        String school = view.getFirstSchoolField().getText().trim();
        try {
            UserDAOForgot dao = new UserDAOForgot();
            if (dao.saveSecurityQuestions(username, color, school)) {
                JOptionPane.showMessageDialog(view, "Security questions saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Unable to save, please try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() { view.setVisible(true); }
}
