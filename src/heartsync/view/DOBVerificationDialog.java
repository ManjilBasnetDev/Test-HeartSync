package heartsync.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class DOBVerificationDialog extends JDialog {
    private JComboBox<Integer> yearBox;
    private JComboBox<Integer> monthBox;
    private JComboBox<Integer> dayBox;
    private JLabel ageLabel;
    private JButton okButton;
    private int age = -1;
    private boolean confirmed = false;

    public DOBVerificationDialog(JFrame parent) {
        super(parent, "Verify Date of Birth", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setSize(350, 220);
        setResizable(false);
        setLocationRelativeTo(parent);

        JLabel prompt = new JLabel("Select your Date of Birth:");
        prompt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.insets = new Insets(10, 10, 10, 10);
        add(prompt, gbc);

        // Date selectors
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) years[i] = currentYear - i;
        yearBox = new JComboBox<>(years);
        monthBox = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
        dayBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) dayBox.addItem(i);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        add(yearBox, gbc);
        gbc.gridx = 1;
        add(monthBox, gbc);
        gbc.gridx = 2;
        add(dayBox, gbc);

        // Age label
        ageLabel = new JLabel("");
        ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        gbc.insets = new Insets(15, 10, 5, 10);
        add(ageLabel, gbc);

        // OK button
        okButton = new JButton("OK");
        gbc.gridy = 3; gbc.insets = new Insets(10, 10, 10, 10);
        add(okButton, gbc);
        okButton.setEnabled(false);

        // Listeners
        ActionListener updateAgeListener = e -> updateAge();
        yearBox.addActionListener(updateAgeListener);
        monthBox.addActionListener(updateAgeListener);
        dayBox.addActionListener(updateAgeListener);

        okButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });
    }

    private void updateAge() {
        Integer year = (Integer) yearBox.getSelectedItem();
        Integer month = (Integer) monthBox.getSelectedItem();
        Integer day = (Integer) dayBox.getSelectedItem();
        if (year == null || month == null || day == null) {
            ageLabel.setText("");
            okButton.setEnabled(false);
            return;
        }
        try {
            LocalDate dob = LocalDate.of(year, month, day);
            LocalDate now = LocalDate.now();
            age = Period.between(dob, now).getYears();
            ageLabel.setText("You are " + age + " years old");
            ageLabel.setForeground(new Color(0, 150, 0));
            okButton.setEnabled(true);
        } catch (Exception ex) {
            ageLabel.setText("");
            okButton.setEnabled(false);
        }
    }

    public int getAge() {
        return age;
    }
    public boolean isConfirmed() {
        return confirmed;
    }
}
