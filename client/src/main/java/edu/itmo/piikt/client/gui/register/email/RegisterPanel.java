package edu.itmo.piikt.client.gui.register.email;

import edu.itmo.piikt.client.gui.RightContentPanel;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private RightContentPanel parent;
    private JTextField emailField;

    public RegisterPanel(RightContentPanel parent) {
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
        JPanel emailLabelPanel = new JPanel();
        emailLabelPanel.setLayout(new BoxLayout(emailLabelPanel, BoxLayout.X_AXIS));
        emailLabelPanel.setOpaque(false);
        emailLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabelPanel.setMaximumSize(new Dimension(fixedWidth, 30));
        emailLabelPanel.setPreferredSize(new Dimension(fixedWidth, 30));

        JLabel emailLabel = new JLabel("Введите электронную почту");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        emailLabelPanel.add(emailLabel);
        emailLabelPanel.add(Box.createHorizontalGlue());

        formPanel.add(emailLabelPanel);
        formPanel.add(Box.createVerticalStrut(10));

        emailField = new JTextField(30);
        emailField.setFont(new Font("Arial", Font.PLAIN, 18));
        emailField.setBackground(new Color(48, 48, 48));
        emailField.setForeground(Color.WHITE);
        emailField.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        emailField.setCaretColor(Color.WHITE);
        emailField.setHorizontalAlignment(JTextField.LEFT);
        emailField.setMaximumSize(new Dimension(fixedWidth, 60));
        emailField.setPreferredSize(new Dimension(fixedWidth, 60));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(40));

        JButton confirmButton = new JButton("ПОДТВЕРДИТЬ");
        confirmButton.setBackground(new Color(48, 48, 48));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 35));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setMaximumSize(new Dimension(fixedWidth, 80));
        confirmButton.setPreferredSize(new Dimension(fixedWidth, 80));
        confirmButton.addActionListener(e -> onConfirm());
        formPanel.add(confirmButton);
        formPanel.add(Box.createVerticalStrut(15));

        JButton backButton = new JButton("ВЕРНУТЬСЯ НАЗАД");
        backButton.setBackground(new Color(48, 48, 48));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(fixedWidth, 80));
        backButton.setPreferredSize(new Dimension(fixedWidth, 80));
        backButton.addActionListener(e -> parent.showPanel("LOGIN"));
        formPanel.add(backButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(formPanel, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    private void onConfirm() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите email");
            return;
        }
        JOptionPane.showMessageDialog(this, "Код отправлен на " + email);
        parent.showCodeConfirmation(email);
    }
}