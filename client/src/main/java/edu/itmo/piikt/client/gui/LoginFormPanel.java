package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class LoginFormPanel extends JPanel {
	private RightContentPanel parent;
	private JTextField loginField;
	private JPasswordField passwordField;

	public LoginFormPanel(RightContentPanel parent) {
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

		JLabel loginTitleLabel = new JLabel("ВХОД В АККАУНТ");
		loginTitleLabel.setForeground(Color.WHITE);
		loginTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 40, 0);
		add(loginTitleLabel, gbc);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int fixedWidth = 500;

		JPanel loginFieldPanel = createFieldPanel("Введите логин", loginField = new JTextField(30));
		formPanel.add(loginFieldPanel);
		formPanel.add(Box.createVerticalStrut(20));

		JPanel passwordFieldPanel = createFieldPanel("Введите пароль", passwordField = new JPasswordField(30));
		formPanel.add(passwordFieldPanel);
		formPanel.add(Box.createVerticalStrut(30));

		JButton loginButton = new JButton("ВОЙТИ В АККАУНТ");
		loginButton.setBackground(new Color(48, 48, 48));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Arial", Font.BOLD, 30));
		loginButton.setFocusPainted(false);
		loginButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setMaximumSize(new Dimension(fixedWidth, 80));
		loginButton.setPreferredSize(new Dimension(fixedWidth, 80));
		loginButton.addActionListener(e -> onLogin());
		formPanel.add(loginButton);
		formPanel.add(Box.createVerticalStrut(15));

		JButton forgotButton = new JButton("ВОССТАНОВИТЬ ПАРОЛЬ");
		forgotButton.setBackground(new Color(48, 48, 48));
		forgotButton.setForeground(Color.WHITE);
		forgotButton.setFont(new Font("Arial", Font.BOLD, 25));
		forgotButton.setFocusPainted(false);
		forgotButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		forgotButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		forgotButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		forgotButton.setMaximumSize(new Dimension(fixedWidth, 70));
		forgotButton.setPreferredSize(new Dimension(fixedWidth, 70));
		forgotButton.addActionListener(e -> parent.showPanel("FORGOT_PASSWORD"));
		formPanel.add(forgotButton);
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
		backButton.addActionListener(e -> parent.showPanel("LOGIN_START"));
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

	private void onLogin() {
		String login = loginField.getText().trim();
		String password = new String(passwordField.getPassword());

		if (login.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите логин");
			return;
		}
		if (password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите пароль");
			return;
		}

		parent.showMainApp(login);
	}
}
