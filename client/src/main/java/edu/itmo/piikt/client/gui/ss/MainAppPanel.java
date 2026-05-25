package edu.itmo.piikt.client.gui.ss;

import edu.itmo.piikt.client.gui.command.HelpPanel;
import edu.itmo.piikt.client.gui.command.InfoPanel;
import edu.itmo.piikt.client.gui.command.HistoryPanel;
import edu.itmo.piikt.client.gui.command.FirstWorkerPanel;
import edu.itmo.piikt.client.gui.command.ReadFilePanel;
import edu.itmo.piikt.client.gui.command.SearchByOrganizationPanel;
import edu.itmo.piikt.client.gui.command.SearchResultPanel;
import edu.itmo.piikt.client.gui.command.ShowPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
public class MainAppPanel extends JPanel {
	private CardLayout cardLayout;
	private JPanel container;
	private String currentUser;
	private HelpPanel helpPanel;
	private InfoPanel infoPanel;
	private HistoryPanel historyPanel;
	private FirstWorkerPanel firstWorkerPanel;
	private ReadFilePanel readFilePanel;
	private SearchByOrganizationPanel searchByOrganizationPanel;
	private ShowPanel showPanel;

	public MainAppPanel() {
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		cardLayout = new CardLayout();
		container = new JPanel(cardLayout);
		container.setBackground(Color.BLACK);
		container.add(createWelcomePanel(), "WELCOME");
		add(container, BorderLayout.CENTER);
	}

	private JPanel createWelcomePanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.BLACK);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;

		JLabel welcomeLabel = new JLabel();
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
		welcomeLabel.setText("<html><body style='text-align: center; width: 500px;'>" + "Вы вошли в аккаунт!<br><br>"
				+ "Для поиска информации о командах воспользуйтесь командой помощь.<br>"
				+ "Для этого необходимо перейти в меню обозначенное как ☰ и выбрать команду." + "</body></html>");

		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 30, 0);
		panel.add(welcomeLabel, gbc);

		ImageIcon gearIcon = loadImage("images/gear2.png");
		if (gearIcon != null) {
			Image img = gearIcon.getImage();
			Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
			JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 0, 0);
			panel.add(imageLabel, gbc);
		}

		return panel;
	}

	public void showHelp(String username) {
		this.currentUser = username;
		if (helpPanel == null) {
			helpPanel = new HelpPanel(this, username);
			container.add(helpPanel, "HELP");
		}
		cardLayout.show(container, "HELP");
	}

	public void showInfo(String username) {
		this.currentUser = username;
		if (infoPanel == null) {
			infoPanel = new InfoPanel(this, username);
			container.add(infoPanel, "INFO");
		}
		cardLayout.show(container, "INFO");
	}

	public void showHistory(String username) {
		this.currentUser = username;
		if (historyPanel == null) {
			historyPanel = new HistoryPanel(this, username);
			container.add(historyPanel, "HISTORY");
		}
		cardLayout.show(container, "HISTORY");
	}

	public void showFirstWorker(String username) {
		this.currentUser = username;
		if (firstWorkerPanel == null) {
			firstWorkerPanel = new FirstWorkerPanel(this, username);
			container.add(firstWorkerPanel, "FIRST_WORKER");
		}
		cardLayout.show(container, "FIRST_WORKER");
	}

	public void showReadFile(String username) {
		this.currentUser = username;
		if (readFilePanel == null) {
			readFilePanel = new ReadFilePanel(this, username);
			container.add(readFilePanel, "READ_FILE");
		}
		cardLayout.show(container, "READ_FILE");
	}

	public void showSearchByOrganization(String username) {
		this.currentUser = username;
		if (searchByOrganizationPanel == null) {
			searchByOrganizationPanel = new SearchByOrganizationPanel(this, username);
			container.add(searchByOrganizationPanel, "SEARCH_BY_ORGANIZATION");
		}
		cardLayout.show(container, "SEARCH_BY_ORGANIZATION");
	}

	public void showSearchResult(String username) {
		this.currentUser = username;
		SearchResultPanel searchResultPanel = new SearchResultPanel(this, username);
		container.add(searchResultPanel, "SEARCH_RESULT");
		cardLayout.show(container, "SEARCH_RESULT");
	}

	public void showShow(String username) {
		this.currentUser = username;
		showPanel = new ShowPanel(this, username);
		container.add(showPanel, "SHOW");
		cardLayout.show(container, "SHOW");
	}

	public void addWorkerToTable(Object[] workerData) {
		if (showPanel != null) {
			showPanel.addRow(workerData);
		}
	}

	public void updateWorkerInTable(int rowIndex, Object[] workerData) {
		if (showPanel != null) {
			showPanel.updateRow(rowIndex, workerData);
		}
	}

	private ImageIcon loadImage(String path) {
		try {
			URL imgUrl = getClass().getClassLoader().getResource(path);
			if (imgUrl != null) {
				return new ImageIcon(imgUrl);
			}
		} catch (Exception ignored) {
			// ignored
		}
		return null;
	}
}
