package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordPanel extends JPanel {
	private final RightContentPanel parent;
	private final LocaleManager lm;
	private final JTextField loginField;
	private final JTextField emailField;
	private final JLabel forgotTitleLabel;
	private JLabel loginLabel;
	private JLabel emailLabel;
	private final JButton sendButton;
	private final JButton backButton;

	public ForgotPasswordPanel(RightContentPanel parent) {
		this.parent = parent;
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

		forgotTitleLabel = new JLabel();
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

		JPanel loginPanel = createFieldPanel(loginField = new JTextField(30));
		formPanel.add(loginPanel);
		formPanel.add(Box.createVerticalStrut(20));

		JPanel emailPanel = createFieldPanel(emailField = new JTextField(30));
		formPanel.add(emailPanel);
		formPanel.add(Box.createVerticalStrut(40));

		sendButton = createSendButton(fixedWidth);
		formPanel.add(sendButton);
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
		forgotTitleLabel.setText(lm.getString("auth.recovery"));
		loginLabel.setText(lm.getString("auth.login_placeholder"));
		emailLabel.setText(lm.getString("auth.email"));
		sendButton.setText(lm.getString("button.send_code"));
		backButton.setText(lm.getString("button.back"));
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

		if (field == loginField) {
			loginLabel = label;
		} else if (field == emailField) {
			emailLabel = label;
		}

		return panel;
	}

	private JButton createSendButton(int fixedWidth) {
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
		button.addActionListener(e -> onSendCode());
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
		button.addActionListener(e -> parent.showLoginStart());
		return button;
	}

	private void onSendCode() {
		String login = loginField.getText().trim();
		String email = emailField.getText().trim();

		if (login.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_login"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (email.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_email"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		setEnabled(false);

		new Thread(() -> {
			try {
				ClientCommand checkCommand = ClientCommand.builder().nameCommand("check_user").login(login).email(email)
						.build();

				ServerResponse checkResponse = GuiCommandSender.INSTANCE.sendCommand(checkCommand);

				SwingUtilities.invokeLater(() -> {
					setEnabled(true);

					if (checkResponse != null && checkResponse.execution()) {
						parent.showResetCodeConfirmation(login, email);
					} else {
						String errorMsg = checkResponse != null
								? checkResponse.message()
								: lm.getString("error.user_not_found");
						JOptionPane.showMessageDialog(this, errorMsg, lm.getString("message.error"),
								JOptionPane.ERROR_MESSAGE);
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(() -> {
					setEnabled(true);
					JOptionPane.showMessageDialog(this, lm.getString("error.connection") + ": " + ex.getMessage(),
							lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}
}
