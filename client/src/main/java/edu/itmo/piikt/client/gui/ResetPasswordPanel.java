package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;

public class ResetPasswordPanel extends JPanel {
	private final RightContentPanel parent;
	private final String login;
	private final String email;
	private final LocaleManager lm;
	private final JPasswordField newPasswordField;
	private final JPasswordField confirmPasswordField;
	private final JLabel resetTitleLabel;
	private JLabel loginInfoLabel;
	private JLabel emailInfoLabel;
	private JLabel newPasswordLabel;
	private JLabel confirmPasswordLabel;
	private final JButton saveButton;
	private final JButton backButton;

	public ResetPasswordPanel(RightContentPanel parent, String login, String email) {
		this.parent = parent;
		this.login = login;
		this.email = email;
		this.lm = LocaleManager.getInstance();
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
		resetTitleLabel = new JLabel();
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

		JPanel infoPanel = createInfoPanel();

		formPanel.add(infoPanel);
		formPanel.add(Box.createVerticalStrut(30));

		JPanel newPasswordPanel = createFieldPanel(newPasswordField = new JPasswordField(30));
		formPanel.add(newPasswordPanel);
		formPanel.add(Box.createVerticalStrut(20));

		JPanel confirmPanel = createFieldPanel(confirmPasswordField = new JPasswordField(30));
		formPanel.add(confirmPanel);
		formPanel.add(Box.createVerticalStrut(40));

		saveButton = createSaveButton(fixedWidth);
		formPanel.add(saveButton);
		formPanel.add(Box.createVerticalStrut(15));

		backButton = createBackButton(fixedWidth);
		formPanel.add(backButton);

		gbc.gridy = 2;
		gbc.insets = new Insets(30, 0, 0, 0);
		add(formPanel, gbc);

		gbc.gridy = 3;
		gbc.weighty = 1.0;
		add(Box.createVerticalGlue(), gbc);
		lm.addLocaleChangeListener(this::updateTexts);
		updateTexts();
	}

	private void updateTexts() {
		resetTitleLabel.setText(lm.getString("auth.recovery"));
		loginInfoLabel.setText(lm.getString("auth.login_placeholder") + ": " + login);
		emailInfoLabel.setText(lm.getString("auth.email") + ": " + email);
		newPasswordLabel.setText(lm.getString("reset.new_password"));
		confirmPasswordLabel.setText(lm.getString("reset.confirm_new_password"));
		saveButton.setText(lm.getString("button.save"));
		backButton.setText(lm.getString("button.back"));
	}

	private JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);
		infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		loginInfoLabel = new JLabel();
		loginInfoLabel.setForeground(new Color(150, 150, 150));
		loginInfoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		loginInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(loginInfoLabel);

		emailInfoLabel = new JLabel();
		emailInfoLabel.setForeground(new Color(150, 150, 150));
		emailInfoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		emailInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(emailInfoLabel);

		return infoPanel;
	}

	private JPanel createFieldPanel(JTextField field) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.setMaximumSize(new Dimension(500, 80));

		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		labelPanel.setOpaque(false);
		JLabel label = new JLabel();
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

		if (field == newPasswordField) {
			newPasswordLabel = label;
		} else if (field == confirmPasswordField) {
			confirmPasswordLabel = label;
		}
		return panel;
	}

	private JButton createSaveButton(int fixedWidth) {
		JButton button = new JButton();
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 30));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(fixedWidth, 80));
		button.setPreferredSize(new Dimension(fixedWidth, 80));
		button.addActionListener(e -> onSavePassword());
		return button;
	}

	private JButton createBackButton(int fixedWidth) {
		JButton button = new JButton();
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 30));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(fixedWidth, 80));
		button.setPreferredSize(new Dimension(fixedWidth, 80));
		button.addActionListener(e -> parent.showForgotPassword());
		return button;
	}

	private void onSavePassword() {
		String newPassword = new String(newPasswordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());

		if (newPassword.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_password"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (newPassword.length() < 8) {
			JOptionPane.showMessageDialog(this, lm.getString("error.password_too_short"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!newPassword.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(this, lm.getString("error.password_mismatch"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			ClientCommand command = ClientCommand.builder().nameCommand("reset_password").login(login).email(email)
					.password(newPassword).build();

			ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

			if (response != null && response.execution()) {
				JOptionPane.showMessageDialog(this, lm.getString("message.password_changed"),
						lm.getString("message.success"), JOptionPane.INFORMATION_MESSAGE);
				parent.showMainApp(login);
			} else {
				String errorMsg = response != null ? response.message() : lm.getString("error.unknown");
				JOptionPane.showMessageDialog(this, lm.getString("error.prefix") + errorMsg,
						lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, lm.getString("error.connection") + ": " + ex.getMessage(),
					lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
