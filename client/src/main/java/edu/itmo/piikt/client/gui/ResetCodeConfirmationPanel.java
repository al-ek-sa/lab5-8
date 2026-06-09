package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;

public class ResetCodeConfirmationPanel extends JPanel {
	private final RightContentPanel parent;
	private final LocaleManager lm;
	private final JTextField[] codeFields;
	private final String email;
	private final String login;
	private final String generatedCode;
	private final JLabel confirmTitleLabel;
	private JLabel messageLabel;
	private JLabel emailLabel;
	private final JButton confirmButton;
	private final JButton backButton;

	public ResetCodeConfirmationPanel(RightContentPanel parent, String login, String email) {
		this.parent = parent;
		this.login = login;
		this.email = email;
		this.lm = LocaleManager.getInstance();
		this.generatedCode = String.format("%06d", new SecureRandom().nextInt(1000000));

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

		confirmTitleLabel = new JLabel();
		confirmTitleLabel.setForeground(Color.WHITE);
		confirmTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 40, 0);
		add(confirmTitleLabel, gbc);
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int fixedWidth = 500;
		JPanel messagePanel = createMessagePanel(fixedWidth);

		formPanel.add(messagePanel);
		formPanel.add(Box.createVerticalStrut(30));

		JPanel codePanel = new JPanel();
		codePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
		codePanel.setOpaque(false);
		codePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		codeFields = new JTextField[6];
		for (int i = 0; i < 6; i++) {
			final int index = i;
			codeFields[i] = new JTextField(1);
			codeFields[i].setFont(new Font("Arial", Font.BOLD, 30));
			codeFields[i].setBackground(new Color(48, 48, 48));
			codeFields[i].setForeground(Color.WHITE);
			codeFields[i].setHorizontalAlignment(JTextField.CENTER);
			codeFields[i].setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
			codeFields[i].setPreferredSize(new Dimension(60, 60));
			codeFields[i].setMaximumSize(new Dimension(60, 60));

			codeFields[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if (!Character.isDigit(c)) {
						e.consume();
						return;
					}
					if (!codeFields[index].getText().isEmpty()) {
						codeFields[index].setText("");
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					String text = codeFields[index].getText();
					if (text.length() == 1 && index < 5) {
						codeFields[index + 1].requestFocus();
					}
					if (text.isEmpty() && index > 0 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						codeFields[index - 1].requestFocus();
					}
				}
			});

			codePanel.add(codeFields[i]);
		}
		formPanel.add(codePanel);
		formPanel.add(Box.createVerticalStrut(40));

		confirmButton = createConfirmButton(fixedWidth);
		formPanel.add(confirmButton);
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

		sendCodeToServer();
	}

	private void updateTexts() {
		confirmTitleLabel.setText(lm.getString("auth.confirm"));
		messageLabel.setText(lm.getString("reset.enter_code"));
		emailLabel.setText(lm.getString("reset.code_sent_to") + " " + email);
		confirmButton.setText(lm.getString("button.confirm"));
		backButton.setText(lm.getString("button.back"));
	}

	private JPanel createMessagePanel(int fixedWidth) {
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.setOpaque(false);
		messagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messagePanel.setMaximumSize(new Dimension(fixedWidth, 100));
		messagePanel.setPreferredSize(new Dimension(fixedWidth, 100));

		messageLabel = new JLabel();
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messagePanel.add(messageLabel);

		emailLabel = new JLabel();
		emailLabel.setForeground(new Color(150, 150, 150));
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messagePanel.add(emailLabel);
		return messagePanel;
	}

	private JButton createConfirmButton(int fixedWidth) {
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
		button.addActionListener(e -> onConfirmCode());
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

	private void sendCodeToServer() {
		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("send_reset_code").login(login).email(email)
						.data(generatedCode).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				if (response != null && !response.execution()) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
							lm.getString("error.send_code_failed") + ": " + response.message(),
							lm.getString("message.error"), JOptionPane.ERROR_MESSAGE));
				}
			} catch (Exception ex) {
				SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
						lm.getString("error.connection") + ": " + ex.getMessage(), lm.getString("message.error"),
						JOptionPane.ERROR_MESSAGE));
			}
		}).start();
	}

	private void onConfirmCode() {
		StringBuilder code = new StringBuilder();
		for (JTextField field : codeFields) {
			if (field.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, lm.getString("error.enter_full_code"),
						lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			code.append(field.getText().trim());
		}

		if (!code.toString().equals(generatedCode)) {
			JOptionPane.showMessageDialog(this, lm.getString("error.invalid_code"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		parent.showResetPasswordPanel(login, email);
	}
}
