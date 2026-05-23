package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordPanel extends JPanel {
    private RightContentPanel parent;
    private JTextField loginField;
    private JTextField emailField;

    public ForgotPasswordPanel(RightContentPanel parent) {
        this.parent = parent;
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
        JLabel forgotTitleLabel = new JLabel("ВОССТАНОВЛЕНИЕ ПАРОЛЯ");
        forgotTitleLabel.setForeground(Color.WHITE);
        forgotTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(forgotTitleLabel, gbc);
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int fixedWidth = 500;
        JPanel loginFieldPanel = createFieldPanel("Введите логин", loginField = new JTextField(30));
        formPanel.add(loginFieldPanel);
        formPanel.add(Box.createVerticalStrut(20));

        JPanel emailFieldPanel = createFieldPanel("Введите электронную почту", emailField = new JTextField(30));
        formPanel.add(emailFieldPanel);
        formPanel.add(Box.createVerticalStrut(40));

        JButton resetButton = new JButton("ВОССТАНОВИТЬ ПАРОЛЬ");
        resetButton.setBackground(new Color(48, 48, 48));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 30));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setMaximumSize(new Dimension(fixedWidth, 80));
        resetButton.setPreferredSize(new Dimension(fixedWidth, 80));
        resetButton.addActionListener(e -> onResetPassword());
        formPanel.add(resetButton);
        formPanel.add(Box.createVerticalStrut(15));

        JButton backButton = new JButton("ВЕРНУТЬСЯ НАЗАД");
        backButton.setBackground(new Color(48, 48, 48));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 30));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(fixedWidth, 80));
        backButton.setPreferredSize(new Dimension(fixedWidth, 80));
        backButton.addActionListener(e -> parent.showPanel("LOGIN_FORM"));
        formPanel.add(backButton);

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

    private void onResetPassword() {
        String login = loginField.getText().trim();
        String email = emailField.getText().trim();

        if (login.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите логин");
            return;
        }
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите электронную почту");
            return;
        }
        JOptionPane.showMessageDialog(this, "Код подтверждения отправлен на " + email);

        parent.showResetCodeConfirmation(login, email);
    }
}