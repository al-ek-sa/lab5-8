package edu.itmo.piikt.client.gui.register.email;

import edu.itmo.piikt.client.gui.RightContentPanel;
import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
	private final RightContentPanel parent;
	private final LocaleManager lm;
	private final JTextField emailField;
	private final JLabel registerLabel;
	private final JLabel emailLabel;
	private final JButton confirmButton;
	private final JButton backButton;

	public RegisterPanel(RightContentPanel parent) {
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

		registerLabel = new JLabel();
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

		emailLabel = new JLabel();
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
	}

	private void updateTexts() {
		registerLabel.setText(lm.getString("auth.register"));
		emailLabel.setText(lm.getString("auth.email"));
		confirmButton.setText(lm.getString("button.confirm"));
		backButton.setText(lm.getString("button.back"));
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
		button.addActionListener(e -> onConfirm());
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
		button.addActionListener(e -> parent.showPanel("LOGIN_START"));
		return button;
	}

	private void onConfirm() {
		String email = emailField.getText().trim();
		if (email.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_email"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String code = String.format("%06d", new java.security.SecureRandom().nextInt(1000000));

		try {
			ClientCommand command = ClientCommand.builder().nameCommand("register_email").email(email).data(code)
					.build();

			ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

			if (response != null && response.execution()) {
				parent.showCodeConfirmation(email, code);
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
