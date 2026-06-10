package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.gui.register.email.CodeConfirmationPanel;
import edu.itmo.piikt.client.gui.register.email.CompleteRegistrationPanel;
import edu.itmo.piikt.client.gui.register.email.RegisterPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RightContentPanel extends JPanel {
	private final CardLayout cardLayout;
	private final Map<String, JPanel> panels;
	private final MainGUI mainGUI;

	public RightContentPanel(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		LocaleManager lm = LocaleManager.getInstance();
		this.cardLayout = new CardLayout();
		setLayout(cardLayout);
		setBackground(Color.BLACK);

		panels = new HashMap<>();
		panels.put("LOGIN_START", new LoginPanel(this));
		panels.put("LOGIN_FORM", new LoginFormPanel(this));
		panels.put("FORGOT_PASSWORD", new ForgotPasswordPanel(this));
		panels.put("REGISTER", new RegisterPanel(this));
		panels.put("RESET_CODE_CONFIRMATION", new JPanel());
		panels.put("RESET_PASSWORD", new JPanel());
		panels.put("CODE_CONFIRMATION", new JPanel());
		panels.put("COMPLETE_REGISTRATION", new JPanel());

		for (Map.Entry<String, JPanel> entry : panels.entrySet()) {
			add(entry.getValue(), entry.getKey());
		}

		cardLayout.show(this, "LOGIN_START");
	}

	public void showPanel(String panelName) {
		if (panels.containsKey(panelName)) {
			cardLayout.show(this, panelName);
		}
	}

	public void showMainApp(String username) {
		mainGUI.showAppPanel(username);
	}

	public void showResetCodeConfirmation(String login, String email) {
		ResetCodeConfirmationPanel panel = new ResetCodeConfirmationPanel(this, login, email);
		replacePanel("RESET_CODE_CONFIRMATION", panel);
		cardLayout.show(this, "RESET_CODE_CONFIRMATION");
	}

	public void showResetPasswordPanel(String login, String email) {
		ResetPasswordPanel panel = new ResetPasswordPanel(this, login, email);
		replacePanel("RESET_PASSWORD", panel);
		cardLayout.show(this, "RESET_PASSWORD");
	}

	public void showCodeConfirmation(String email, String code) {
		CodeConfirmationPanel panel = new CodeConfirmationPanel(this, email, code);
		replacePanel("CODE_CONFIRMATION", panel);
		cardLayout.show(this, "CODE_CONFIRMATION");
	}

	public void showCompleteRegistration(String email) {
		CompleteRegistrationPanel panel = new CompleteRegistrationPanel(this, email);
		replacePanel("COMPLETE_REGISTRATION", panel);
		cardLayout.show(this, "COMPLETE_REGISTRATION");
	}

	public void showLoginStart() {
		cardLayout.show(this, "LOGIN_START");
	}

	public void showForgotPassword() {
		cardLayout.show(this, "FORGOT_PASSWORD");
	}

	private void replacePanel(String panelKey, JPanel newPanel) {
		JPanel oldPanel = panels.get(panelKey);
		if (oldPanel != null) {
			remove(oldPanel);
		}
		panels.put(panelKey, newPanel);
		add(newPanel, panelKey);
		revalidate();
		repaint();
	}
}
