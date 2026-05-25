package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.command.HelpPanel;
import edu.itmo.piikt.client.gui.command.InfoPanel;
import edu.itmo.piikt.client.gui.command.HistoryPanel;
import edu.itmo.piikt.client.gui.command.FirstWorkerPanel;
import edu.itmo.piikt.client.gui.command.ReadFilePanel;
import edu.itmo.piikt.client.gui.command.SearchByOrganizationPanel;
import edu.itmo.piikt.client.gui.command.SearchResultPanel;
import edu.itmo.piikt.client.gui.command.ShowPanel;
import edu.itmo.piikt.client.gui.register.email.CodeConfirmationPanel;
import edu.itmo.piikt.client.gui.register.email.CompleteRegistrationPanel;
import edu.itmo.piikt.client.gui.register.email.RegisterPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
@EqualsAndHashCode(callSuper = true)
@Data
public class RightContentPanel extends JPanel {
	private CardLayout cardLayout;
	private Map<String, JPanel> panels;
	private MainGUI mainGUI;
	private String currentUsername;

	public RightContentPanel(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		cardLayout = new CardLayout();
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
		this.currentUsername = username;
		mainGUI.showAppPanel(username);
	}

	public void showResetCodeConfirmation(String login, String email) {
		ResetCodeConfirmationPanel panel = new ResetCodeConfirmationPanel(this, login, email);
		panels.put("RESET_CODE_CONFIRMATION", panel);
		add(panel, "RESET_CODE_CONFIRMATION");
		cardLayout.show(this, "RESET_CODE_CONFIRMATION");
	}

	public void showResetPasswordPanel(String login, String email) {
		ResetPasswordPanel panel = new ResetPasswordPanel(this, login, email);
		panels.put("RESET_PASSWORD", panel);
		add(panel, "RESET_PASSWORD");
		cardLayout.show(this, "RESET_PASSWORD");
	}

	public void showCodeConfirmation(String email, String code) {
		CodeConfirmationPanel panel = new CodeConfirmationPanel(this, email, code);
		panels.put("CODE_CONFIRMATION", panel);
		add(panel, "CODE_CONFIRMATION");
		cardLayout.show(this, "CODE_CONFIRMATION");
	}

	public void showCompleteRegistration(String email) {
		CompleteRegistrationPanel panel = new CompleteRegistrationPanel(this, email);
		panels.put("COMPLETE_REGISTRATION", panel);
		add(panel, "COMPLETE_REGISTRATION");
		cardLayout.show(this, "COMPLETE_REGISTRATION");
	}

	public void showLoginStart() {
		cardLayout.show(this, "LOGIN_START");
	}

	public void showLoginForm() {
		cardLayout.show(this, "LOGIN_FORM");
	}

	public void showForgotPassword() {
		cardLayout.show(this, "FORGOT_PASSWORD");
	}

	public void showRegister() {
		cardLayout.show(this, "REGISTER");
	}

	public void resetToLoginStart() {
		cardLayout.show(this, "LOGIN_START");
	}
}
