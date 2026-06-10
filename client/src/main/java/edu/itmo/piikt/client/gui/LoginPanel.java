package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.localization.LocaleManager;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
	private final LocaleManager lm;
	private final JButton loginButton;
	private final JButton registerButton;

	public LoginPanel(RightContentPanel parent) {
		this.lm = LocaleManager.getInstance();

		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;

		JLabel titleLabel = new JLabel("WORKERFLOW");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 90));
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 40, 0);
		add(titleLabel, gbc);

		loginButton = createButton(new Color(48, 48, 48));
		loginButton.addActionListener(e -> parent.showPanel("LOGIN_FORM"));
		gbc.gridy = 1;
		gbc.insets = new Insets(10, 50, 10, 50);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(loginButton, gbc);

		registerButton = createButton(new Color(48, 48, 48));
		registerButton.addActionListener(e -> parent.showPanel("REGISTER"));
		gbc.gridy = 2;
		add(registerButton, gbc);
		lm.addLocaleChangeListener(this::updateTexts);
		updateTexts();
	}

	private void updateTexts() {
		loginButton.setText(lm.getString("auth.login"));
		registerButton.setText(lm.getString("auth.register"));
	}

	private JButton createButton(Color bgColor) {
		JButton button = new JButton();
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 35));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}
}
