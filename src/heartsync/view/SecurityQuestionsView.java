package heartsync.view;

import javax.swing.*;
import java.awt.*;

/**
 * Simple form that lets user enter favourite colour and first school then click Save.
 * Controller will attach listeners and perform validation.  Public getters provide access.
 */
public class SecurityQuestionsView extends JDialog {
    private final JTextField favoriteColorField = new JTextField();
    private final JTextField firstSchoolField = new JTextField();
    private final JButton saveButton = new JButton("Save");

    public SecurityQuestionsView(Frame parent) {
        super(parent, "Set Security Questions", true);
        setSize(500, 450);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Theme similar to others: pink outer background, white rounded inner panel
        getContentPane().setBackground(new Color(255, 219, 227));
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(null);
        inner.setBounds(50, 30, 400, 340);
        add(inner);

        JLabel title = new JLabel("SET SECURITY QUESTIONS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(0, 10, 400, 30);
        inner.add(title);

        JLabel colorLabel = new JLabel("Favourite colour:");
        colorLabel.setBounds(20, 70, 200, 25);
        inner.add(colorLabel);
        favoriteColorField.setBounds(20, 100, 360, 35);
        inner.add(favoriteColorField);

        JLabel schoolLabel = new JLabel("First school:");
        schoolLabel.setBounds(20, 150, 200, 25);
        inner.add(schoolLabel);
        firstSchoolField.setBounds(20, 180, 360, 35);
        inner.add(firstSchoolField);

        saveButton.setBounds(140, 250, 120, 40);
        saveButton.setEnabled(false);
        inner.add(saveButton);

        saveButton.addActionListener(e -> {
            // ... existing code for validation and saving ...
            saved = true;
            JOptionPane.showMessageDialog(this, "Security questions saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }

    private boolean saved = false;
    public JTextField getFavoriteColorField() { return favoriteColorField; }
    public JTextField getFirstSchoolField() { return firstSchoolField; }
    public JButton getSaveButton() { return saveButton; }
    public void markSaved() { this.saved = true; }
    public boolean isSaved() { return saved; }
}
