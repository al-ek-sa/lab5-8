package edu.itmo.piikt.client.gui.register.email;

import edu.itmo.piikt.client.gui.RightContentPanel;

import javax.swing.*;
import java.awt.*;

public class CompleteRegistrationPanel extends JPanel {
    private RightContentPanel parent;
    private String email;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public CompleteRegistrationPanel(RightContentPanel parent, String email) {
        this.parent = parent;
        this.email = email;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        JLabel titleLabel = new JLabel("WORKERFLOW");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 0, 20, 0);
        add(titleLabel, gbc);
        JLabel registerLabel = new JLabel("РЕГИСТРАЦИЯ");
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(registerLabel, gbc);
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int fixedWidth = 500;
        JPanel loginPanel = createFieldPanel("Введите логин", loginField = new JTextField(30));
        formPanel.add(loginPanel);
        formPanel.add(Box.createVerticalStrut(20));
        JPanel passwordPanel = createFieldPanel("Введите пароль", passwordField = new JPasswordField(30));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(20));
        JPanel confirmPanel = createFieldPanel("Введите пароль повторно", confirmPasswordField = new JPasswordField(30));
        formPanel.add(confirmPanel);
        formPanel.add(Box.createVerticalStrut(40));
        JButton completeButton = new JButton("ЗАВЕРШИТЬ РЕГИСТРАЦИЮ");
        completeButton.setBackground(new Color(48, 48, 48));
        completeButton.setForeground(Color.WHITE);
        completeButton.setFont(new Font("Arial", Font.BOLD, 35));
        completeButton.setFocusPainted(false);
        completeButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        completeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        completeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        completeButton.setMaximumSize(new Dimension(fixedWidth, 80));
        completeButton.setPreferredSize(new Dimension(fixedWidth, 80));
        completeButton.addActionListener(e -> onCompleteRegistration());
        formPanel.add(completeButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(formPanel, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(500, 80));
        panel.setPreferredSize(new Dimension(500, 80));

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        labelPanel.add(label);
        panel.add(labelPanel);
        panel.add(Box.createVerticalStrut(5));

        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setBackground(new Color(48, 48, 48));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        field.setCaretColor(Color.WHITE);
        field.setMaximumSize(new Dimension(500, 50));
        field.setPreferredSize(new Dimension(500, 50));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);

        return panel;
    }

    private void onCompleteRegistration() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (login.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите логин");
            return;
        }
        if (login.length() < 8) {
            JOptionPane.showMessageDialog(this, "Логин должен быть не менее 8 символов");
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите пароль");
            return;
        }
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Пароль должен быть не менее 8 символов");
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Пароли не совпадают");
            return;
        }
        parent.showMainApp(login);
    }
}