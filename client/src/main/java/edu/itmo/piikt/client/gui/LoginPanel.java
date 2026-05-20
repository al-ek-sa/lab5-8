package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("ВХОД");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Логин:"), gbc);

        gbc.gridx = 1;
        loginField = new JTextField(15);
        add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Пароль:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        JButton loginButton = new JButton("Войти");
        loginButton.setBackground(new Color(70, 130, 200));
        loginButton.setForeground(Color.WHITE);

        JButton registerButton = new JButton("Регистрация");
        registerButton.setBackground(new Color(100, 150, 220));
        registerButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }
}