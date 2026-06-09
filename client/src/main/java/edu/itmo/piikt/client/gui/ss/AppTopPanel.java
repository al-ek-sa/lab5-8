package edu.itmo.piikt.client.gui.ss;

import edu.itmo.piikt.client.gui.MainGUI;
import edu.itmo.piikt.client.gui.localization.LocaleManager;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class AppTopPanel extends JPanel {
	private final JLabel userLabel;
	private final JButton langButton;
	private String currentUser;
	@Setter
	private MainAppPanel mainAppPanel;
	private final JPopupMenu commandMenu;
	private final LocaleManager localeManager;
	private final MainGUI mainGUI;

	public AppTopPanel(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		this.localeManager = LocaleManager.getInstance();
		setLayout(new BorderLayout());
		setBackground(new Color(20, 20, 30));
		setPreferredSize(new Dimension(0, 60));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		leftPanel.setOpaque(false);

		JButton menuButton = new JButton("☰");
		menuButton.setFont(new Font("Arial", Font.BOLD, 24));
		menuButton.setFocusPainted(false);
		menuButton.setBackground(new Color(20, 20, 30));
		menuButton.setForeground(Color.WHITE);
		menuButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		commandMenu = new JPopupMenu();
		updateCommandMenu();

		menuButton.addActionListener(e -> commandMenu.show(menuButton, 0, menuButton.getHeight()));
		leftPanel.add(menuButton);

		JLabel titleLabel = new JLabel("WORKERFLOW");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		leftPanel.add(titleLabel);

		add(leftPanel, BorderLayout.WEST);

		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
		buttonPanel.setOpaque(false);

		userLabel = new JLabel("");
		userLabel.setForeground(Color.WHITE);
		userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		buttonPanel.add(userLabel);

		langButton = new JButton(getLangCode());
		langButton.setFont(new Font("Arial", Font.BOLD, 12));
		langButton.setFocusPainted(false);
		langButton.setBackground(new Color(60, 60, 70));
		langButton.setForeground(Color.WHITE);
		langButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		langButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPopupMenu langMenu = getJPopupMenu();
		langButton.addActionListener(e -> langMenu.show(langButton, 0, langButton.getHeight()));
		buttonPanel.add(langButton);

		JButton logoutButton = getJButton();
		buttonPanel.add(logoutButton);

		rightPanel.add(buttonPanel, BorderLayout.EAST);
		add(rightPanel, BorderLayout.EAST);

		localeManager.addLocaleChangeListener(() -> SwingUtilities.invokeLater(() -> {
			updateCommandMenu();
			langButton.setText(getLangCode());
		}));
	}

	@Nonnull
	private JButton getJButton() {
		JButton logoutButton = new JButton("->");
		logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
		logoutButton.setFocusPainted(false);
		logoutButton.setBackground(new Color(60, 60, 70));
		logoutButton.setForeground(Color.WHITE);
		logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
		logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logoutButton.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(this, localeManager.getString("confirm.logout"),
					localeManager.getString("confirm.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION && mainGUI != null) {
				mainGUI.logout();
			}
		});
		return logoutButton;
	}

	private String getLangCode() {
		String lang = localeManager.getCurrentLang();
		return switch (lang) {
			case "de" -> "DE";
			case "sv" -> "SV";
			case "es" -> "ES";
			default -> "RU";
		};
	}

	@Nonnull
	private JPopupMenu getJPopupMenu() {
		JPopupMenu langMenu = new JPopupMenu();
		JMenuItem ruItem = new JMenuItem("Русский (RU)");
		JMenuItem deItem = new JMenuItem("Deutsch (DE)");
		JMenuItem svItem = new JMenuItem("Svenska (SV)");
		JMenuItem esItem = new JMenuItem("Español (ES)");

		ruItem.addActionListener(e -> {
			localeManager.setLocale("ru");
			updateCommandMenu();
		});

		deItem.addActionListener(e -> {
			localeManager.setLocale("de");
			updateCommandMenu();
		});

		svItem.addActionListener(e -> {
			localeManager.setLocale("sv");
			updateCommandMenu();
		});

		esItem.addActionListener(e -> {
			localeManager.setLocale("es");
			updateCommandMenu();
		});

		langMenu.add(ruItem);
		langMenu.add(deItem);
		langMenu.add(svItem);
		langMenu.add(esItem);
		return langMenu;
	}

	private void updateCommandMenu() {
		commandMenu.removeAll();

		commandMenu.add(createCommandMenuItem("search_by_organization",
				localeManager.getString("command.search_organization")));
		commandMenu.add(createCommandMenuItem("help", localeManager.getString("command.help")));
		commandMenu.add(createCommandMenuItem("info", localeManager.getString("command.info")));
		commandMenu.add(createCommandMenuItem("history", localeManager.getString("command.history")));
		commandMenu.add(createCommandMenuItem("animation", localeManager.getString("command.animation")));
		commandMenu.add(createCommandMenuItem("first_worker", localeManager.getString("command.first_worker")));
		commandMenu.add(createCommandMenuItem("read_file", localeManager.getString("command.read_file")));
		commandMenu.add(createCommandMenuItem("show", localeManager.getString("command.show")));

		commandMenu.revalidate();
		commandMenu.repaint();
	}

	private JMenuItem createCommandMenuItem(String command, String description) {
		JMenuItem item = new JMenuItem(description);
		item.addActionListener(e -> {
			if (mainAppPanel == null) {
				JOptionPane.showMessageDialog(this, localeManager.getString("error.panel_not_initialized"),
						localeManager.getString("message.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			edu.itmo.piikt.client.command.history.HistoryCommands.INSTANCE.add(command);

			switch (command) {
				case "help" -> mainAppPanel.showHelp(currentUser);
				case "info" -> mainAppPanel.showInfo(currentUser);
				case "history" -> mainAppPanel.showHistory(currentUser);
				case "first_worker" -> mainAppPanel.showFirstWorker(currentUser);
				case "read_file" -> mainAppPanel.showReadFile(currentUser);
				case "search_by_organization" -> mainAppPanel.showSearchByOrganization(currentUser);
				case "show" -> mainAppPanel.showShow(currentUser);
				case "animation" -> mainAppPanel.showAnimation(currentUser);
				default -> JOptionPane.showMessageDialog(this,
						localeManager.getString("message.command_not_implemented") + " '" + description + "'",
						localeManager.getString("message.info"), JOptionPane.INFORMATION_MESSAGE);
			}
		});
		return item;
	}

	public void setUsername(String username) {
		this.currentUser = username;
		String displayName = username;
		if (displayName.length() > 15) {
			displayName = displayName.substring(0, 12) + "...";
		}
		userLabel.setText(displayName);
	}
}
