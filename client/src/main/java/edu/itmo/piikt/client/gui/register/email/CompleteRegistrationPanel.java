package edu.itmo.piikt.client.gui.register.email;

import edu.itmo.piikt.client.gui.RightContentPanel;
import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
public class CompleteRegistrationPanel extends JPanel {
	private final RightContentPanel parent;
	private final String email;
	private final LocaleManager lm;
	private final JTextField loginField;
	private final JPasswordField passwordField;
	private final JPasswordField confirmPasswordField;
	private final JLabel registerLabel;
	private JLabel loginLabel;
	private JLabel passwordLabel;
	private JLabel confirmPasswordLabel;
	private final JButton completeButton;

	public CompleteRegistrationPanel(RightContentPanel parent, String email) {
		this.parent = parent;
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

		JPanel loginPanel = createFieldPanel(loginField = new JTextField(30));
		formPanel.add(loginPanel);
		formPanel.add(Box.createVerticalStrut(20));

		JPanel passwordPanel = createFieldPanel(passwordField = new JPasswordField(30));
		formPanel.add(passwordPanel);
		formPanel.add(Box.createVerticalStrut(20));

		JPanel confirmPanel = createFieldPanel(confirmPasswordField = new JPasswordField(30));
		formPanel.add(confirmPanel);
		formPanel.add(Box.createVerticalStrut(40));

		completeButton = createCompleteButton(fixedWidth);
		formPanel.add(completeButton);

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
		loginLabel.setText(lm.getString("auth.login_placeholder"));
		passwordLabel.setText(lm.getString("auth.password_placeholder"));
		confirmPasswordLabel.setText(lm.getString("auth.confirm_password"));
		completeButton.setText(lm.getString("button.complete"));
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
		} else if (field == passwordField) {
			passwordLabel = label;
		} else if (field == confirmPasswordField) {
			confirmPasswordLabel = label;
		}

		return panel;
	}

	@Nonnull
	private JButton createCompleteButton(int fixedWidth) {
		JButton button = new JButton();
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 25));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(fixedWidth, 80));
		button.setPreferredSize(new Dimension(fixedWidth, 80));
		button.addActionListener(e -> onCompleteRegistration());
		return button;
	}

	private void onCompleteRegistration() {
		String login = loginField.getText().trim();
		String password = new String(passwordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());

		if (login.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_login"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (login.length() < 8) {
			JOptionPane.showMessageDialog(this, lm.getString("error.login_too_short"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (password.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_password"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (password.length() < 8) {
			JOptionPane.showMessageDialog(this, lm.getString("error.password_too_short"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!password.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(this, lm.getString("error.password_mismatch"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			ClientCommand command = ClientCommand.builder().nameCommand("register").user(login).login(login)
					.password(password).email(email).build();

			ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

			if (response != null && response.execution()) {
				JOptionPane.showMessageDialog(this, lm.getString("message.registration_success"),
						lm.getString("message.success"), JOptionPane.INFORMATION_MESSAGE);
				parent.showMainApp(login);
			} else {
				String errorMsg = response != null ? response.message() : lm.getString("error.unknown");
				JOptionPane.showMessageDialog(this, lm.getString("error.registration") + ": " + errorMsg,
						lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, lm.getString("error.connection") + ": " + ex.getMessage(),
					lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
