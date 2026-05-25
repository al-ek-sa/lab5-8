package edu.itmo.piikt.client.gui;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
public class ResetPasswordPanel extends JPanel {
	private RightContentPanel parent;
	private String login;
	private String email;
	private JPasswordField newPasswordField;
	private JPasswordField confirmPasswordField;

	public ResetPasswordPanel(RightContentPanel parent, String login, String email) {
		this.parent = parent;
		this.login = login;
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
		JLabel resetTitleLabel = new JLabel("СМЕНА ПАРОЛЯ");
		resetTitleLabel.setForeground(Color.WHITE);
		resetTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 40, 0);
		add(resetTitleLabel, gbc);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int fixedWidth = 500;

		JPanel infoPanel = getJPanel(login, email);

		formPanel.add(infoPanel);
		formPanel.add(Box.createVerticalStrut(30));

		JPanel newPasswordPanel = createFieldPanel("Введите новый пароль", newPasswordField = new JPasswordField(30));
		formPanel.add(newPasswordPanel);
		formPanel.add(Box.createVerticalStrut(20));

		JPanel confirmPanel = createFieldPanel("Повторите новый пароль", confirmPasswordField = new JPasswordField(30));
		formPanel.add(confirmPanel);
		formPanel.add(Box.createVerticalStrut(40));

		JButton saveButton = getJButton(fixedWidth);
		formPanel.add(saveButton);
		formPanel.add(Box.createVerticalStrut(15));
		JButton backButton = getJButton(parent, fixedWidth);
		formPanel.add(backButton);

		gbc.gridy = 2;
		gbc.insets = new Insets(30, 0, 0, 0);
		add(formPanel, gbc);

		gbc.gridy = 3;
		gbc.weighty = 1.0;
		add(Box.createVerticalGlue(), gbc);
	}

	@Nonnull
	private static JButton getJButton(RightContentPanel parent, int fixedWidth) {
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
		return backButton;
	}

	@Nonnull
	private JButton getJButton(int fixedWidth) {
		JButton saveButton = new JButton("СОХРАНИТЬ ПАРОЛЬ");
		saveButton.setBackground(new Color(48, 48, 48));
		saveButton.setForeground(Color.WHITE);
		saveButton.setFont(new Font("Arial", Font.BOLD, 30));
		saveButton.setFocusPainted(false);
		saveButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.setMaximumSize(new Dimension(fixedWidth, 80));
		saveButton.setPreferredSize(new Dimension(fixedWidth, 80));
		saveButton.addActionListener(e -> onSavePassword());
		return saveButton;
	}

	@Nonnull
	private static JPanel getJPanel(String login, String email) {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);
		infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel loginLabel = new JLabel("Логин: " + login);
		loginLabel.setForeground(new Color(150, 150, 150));
		loginLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(loginLabel);

		JLabel emailInfoLabel = new JLabel("Email: " + email);
		emailInfoLabel.setForeground(new Color(150, 150, 150));
		emailInfoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		emailInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(emailInfoLabel);
		return infoPanel;
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

	private void onSavePassword() {
		String newPassword = new String(newPasswordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());

		if (newPassword.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите новый пароль");
			return;
		}
		if (newPassword.length() < 8) {
			JOptionPane.showMessageDialog(this, "Пароль должен быть не менее 8 символов");
			return;
		}
		if (!newPassword.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(this, "Пароли не совпадают");
			return;
		}
		parent.showMainApp(login);
	}
}
