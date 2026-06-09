package edu.itmo.piikt.client.gui.ss;

import com.fasterxml.jackson.databind.JsonNode;
import edu.itmo.piikt.client.command.history.HistoryCommands;
import edu.itmo.piikt.client.gui.command.*;
import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.common.models.Status;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainAppPanel extends JPanel {
	private final LocaleManager lm;
	private final CardLayout cardLayout;
	private final JPanel container;
	@Setter
	@Getter
	private String currentUser;
	private HelpPanel helpPanel;
	private InfoPanel infoPanel;
	private HistoryPanel historyPanel;
	private FirstWorkerPanel firstWorkerPanel;
	private ReadFilePanel readFilePanel;
	private SearchByOrganizationPanel searchByOrganizationPanel;
	private ShowPanel showPanel;
	private AnimationPanel animationPanel;
	private String currentPanelName = "WELCOME";
	private boolean isLoadingAnimation = false;

	public MainAppPanel() {
		this.lm = LocaleManager.getInstance();
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		cardLayout = new CardLayout();
		container = new JPanel(cardLayout);
		container.setBackground(Color.BLACK);
		container.add(createWelcomePanel(), "WELCOME");
		add(container, BorderLayout.CENTER);
		lm.addLocaleChangeListener(() -> {
			Component welcomePanel = getComponentByName(container, "WELCOME");
			if (welcomePanel != null) {
				container.remove(welcomePanel);
				container.add(createWelcomePanel(), "WELCOME");
			}
			cardLayout.show(container, currentPanelName);
			revalidate();
			repaint();
		});
	}

	private Component getComponentByName(Container container, String name) {
		for (Component comp : container.getComponents()) {
			if (comp.getName() != null && comp.getName().equals(name)) {
				return comp;
			}
		}
		return null;
	}

	private JPanel createWelcomePanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.BLACK);
		panel.setName("WELCOME");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;

		String welcomeText = String.format(
				"<html><body style='text-align: center; width: 500px;'>%s<br><br>%s<br>%s</body></html>",
				lm.getString("message.welcome"), lm.getString("welcome.help_info"), lm.getString("welcome.menu_info"));
		JLabel welcomeLabel = new JLabel(welcomeText);
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));

		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 30, 0);
		panel.add(welcomeLabel, gbc);

		ImageIcon gearIcon = loadImage();
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
		this.currentPanelName = "HELP";
		HistoryCommands.INSTANCE.add("help");
		if (helpPanel == null) {
			helpPanel = new HelpPanel(username);
			container.add(helpPanel, "HELP");
		}
		cardLayout.show(container, "HELP");
	}

	public void showInfo(String username) {
		this.currentUser = username;
		this.currentPanelName = "INFO";
		HistoryCommands.INSTANCE.add("info");
		if (infoPanel == null) {
			infoPanel = new InfoPanel(username);
			container.add(infoPanel, "INFO");
		}
		cardLayout.show(container, "INFO");
	}

	public void showHistory(String username) {
		this.currentUser = username;
		this.currentPanelName = "HISTORY";
		HistoryCommands.INSTANCE.add("history");
		if (historyPanel == null) {
			historyPanel = new HistoryPanel();
			container.add(historyPanel, "HISTORY");
		}
		cardLayout.show(container, "HISTORY");
	}

	public void showFirstWorker(String username) {
		this.currentUser = username;
		this.currentPanelName = "FIRST_WORKER";
		HistoryCommands.INSTANCE.add("first_worker");
		if (firstWorkerPanel == null) {
			firstWorkerPanel = new FirstWorkerPanel(username);
			container.add(firstWorkerPanel, "FIRST_WORKER");
		}
		cardLayout.show(container, "FIRST_WORKER");
	}

	public void showReadFile(String username) {
		this.currentUser = username;
		this.currentPanelName = "READ_FILE";
		HistoryCommands.INSTANCE.add("read_file");
		if (readFilePanel == null) {
			readFilePanel = new ReadFilePanel(username);
			container.add(readFilePanel, "READ_FILE");
		}
		cardLayout.show(container, "READ_FILE");
	}

	public void showSearchByOrganization(String username) {
		this.currentUser = username;
		this.currentPanelName = "SEARCH_BY_ORGANIZATION";
		HistoryCommands.INSTANCE.add("search_by_organization");
		if (searchByOrganizationPanel == null) {
			searchByOrganizationPanel = new SearchByOrganizationPanel(this, username);
			container.add(searchByOrganizationPanel, "SEARCH_BY_ORGANIZATION");
		}
		cardLayout.show(container, "SEARCH_BY_ORGANIZATION");
	}

	public void showSearchResult(String username, String searchData) {
		this.currentUser = username;
		this.currentPanelName = "SEARCH_RESULT";
		HistoryCommands.INSTANCE.add("search_result");
		SearchResultPanel searchResultPanel = new SearchResultPanel(username, searchData);
		container.add(searchResultPanel, "SEARCH_RESULT");
		cardLayout.show(container, "SEARCH_RESULT");
	}

	public void showShow(String username) {
		this.currentUser = username;
		this.currentPanelName = "SHOW";
		HistoryCommands.INSTANCE.add("show");
		if (showPanel == null) {
			showPanel = new ShowPanel(this, username);
			container.add(showPanel, "SHOW");
		}
		cardLayout.show(container, "SHOW");
	}

	public void showAnimation(String username) {
		if (isLoadingAnimation)
			return;

		this.currentUser = username;
		this.currentPanelName = "ANIMATION";
		HistoryCommands.INSTANCE.add("animation");

		if (showPanel != null) {
			if (animationPanel == null) {
				animationPanel = new AnimationPanel(this, username);
				container.add(animationPanel, "ANIMATION");
			}

			List<Worker> workers = showPanel.getWorkersList();
			if (workers != null && !workers.isEmpty()) {
				animationPanel.loadRealWorkers(workers);
			}
			cardLayout.show(container, "ANIMATION");
		} else {
			loadWorkersViaCommand(username);
		}
	}

	private void loadWorkersViaCommand(String username) {
		isLoadingAnimation = true;

		JPanel loadingPanel = new JPanel(new GridBagLayout());
		loadingPanel.setBackground(Color.BLACK);
		loadingPanel.setName("LOADING");
		JLabel loadingLabel = new JLabel(lm.getString("message.loading"));
		loadingLabel.setForeground(Color.WHITE);
		loadingLabel.setFont(new Font("Arial", Font.BOLD, 24));
		loadingPanel.add(loadingLabel);
		container.add(loadingPanel, "LOADING");
		cardLayout.show(container, "LOADING");

		new Thread(() -> {
			try {
				edu.itmo.piikt.common.sc.ClientCommand command = edu.itmo.piikt.common.sc.ClientCommand.builder()
						.nameCommand("show").user(username).build();

				edu.itmo.piikt.common.sc.ServerResponse response = edu.itmo.piikt.client.manager.GuiCommandSender.INSTANCE
						.sendCommand(command);

				SwingUtilities.invokeLater(() -> {
					if (response != null && response.execution() && response.data() != null) {
						List<String> workersData = response.data();
						List<Worker> workers = new ArrayList<>();

						for (String workerStr : workersData) {
							Worker worker = parseWorkerFromString(workerStr);
							if (worker != null) {
								workers.add(worker);
							}
						}

						if (animationPanel == null) {
							animationPanel = new AnimationPanel(this, username);
							container.add(animationPanel, "ANIMATION");
						}
						animationPanel.loadRealWorkers(workers);

						Component loadingComp = getComponentByName(container, "LOADING");
						if (loadingComp != null) {
							container.remove(loadingComp);
						}
						cardLayout.show(container, "ANIMATION");
					} else {
						showLoadingError();
					}
					isLoadingAnimation = false;
				});
			} catch (Exception e) {
				SwingUtilities.invokeLater(() -> {
					showLoadingError();
					isLoadingAnimation = false;
				});
			}
		}).start();
	}

	private void showLoadingError() {
		Component loadingComp = getComponentByName(container, "LOADING");
		if (loadingComp != null) {
			container.remove(loadingComp);
		}
		cardLayout.show(container, currentPanelName);
		JOptionPane.showMessageDialog(this, lm.getString("error.load"), lm.getString("message.error"),
				JOptionPane.ERROR_MESSAGE);
	}

	private Worker parseWorkerFromString(String workerStr) {
		try {
			Worker worker = new Worker();
			worker.setUuid(extractValue(workerStr, "id:"));
			worker.setName(extractValue(workerStr, "name:"));

			String xStr = extractValue(workerStr, "х:");
			if (xStr == null)
				xStr = extractValue(workerStr, "x:");
			String yStr = extractValue(workerStr, "у:");
			if (yStr == null)
				yStr = extractValue(workerStr, "y:");

			if (xStr != null && yStr != null) {
				Coordinates coords = new Coordinates();
				coords.setX(Long.parseLong(xStr));
				coords.setY(Float.parseFloat(yStr));
				worker.setCoordinates(coords);
			}

			String salaryStr = extractValue(workerStr, "salary:");
			if (salaryStr != null && !salaryStr.isEmpty()) {
				worker.setSalary(Float.parseFloat(salaryStr));
			}

			String statusStr = extractValue(workerStr, "status:");
			if (statusStr != null && !statusStr.isEmpty()) {
				try {
					worker.setStatus(Status.valueOf(statusStr));
				} catch (IllegalArgumentException e) {
					// ignored
				}
			}

			return worker;
		} catch (Exception e) {
			return null;
		}
	}

	private String extractValue(String text, String key) {
		int startIndex = text.indexOf(key);
		if (startIndex == -1)
			return null;
		startIndex += key.length();
		while (startIndex < text.length() && text.charAt(startIndex) == ' ')
			startIndex++;
		int endIndex = startIndex;
		while (endIndex < text.length()) {
			char c = text.charAt(endIndex);
			if (c == ',' || (c == ' ' && endIndex + 1 < text.length())) {
				break;
			}
			endIndex++;
		}
		String value = text.substring(startIndex, endIndex).trim();
		if (value.endsWith(","))
			value = value.substring(0, value.length() - 1);
		return value.isEmpty() ? null : value;
	}

	private Worker parseWorkerFromJson(JsonNode node) {
		Worker w = new Worker();
		if (node.has("uuid"))
			w.setUuid(node.get("uuid").asText());
		if (node.has("name"))
			w.setName(node.get("name").asText());
		if (node.has("salary"))
			w.setSalary(node.get("salary").floatValue());
		if (node.has("coordinates") && !node.get("coordinates").isNull()) {
			JsonNode coords = node.get("coordinates");
			Coordinates c = new Coordinates();
			if (coords.has("x"))
				c.setX(coords.get("x").asLong());
			if (coords.has("y"))
				c.setY((float) coords.get("y").asDouble());
			w.setCoordinates(c);
		}

		if (node.has("status") && !node.get("status").isNull()) {
			try {
				w.setStatus(Status.valueOf(node.get("status").asText()));
			} catch (IllegalArgumentException e) {
				// todo
			}
		}

		return w;
	}

	public void updateAnimationWorkers(List<Worker> workers) {
		if (animationPanel != null) {
			animationPanel.loadRealWorkers(workers);
		}
	}

	public void addWorkerFromWebSocket(JsonNode worker) {
		if (showPanel != null) {
			showPanel.addWorkerFromJson(worker);
		}
		if (animationPanel != null && worker != null) {
			Worker w = parseWorkerFromJson(worker);
			animationPanel.addWorker(w);
		}
	}

	public void updateWorkerFromWebSocket(JsonNode worker) {
		if (showPanel != null) {
			showPanel.updateWorkerFromJson(worker);
		}
		if (animationPanel != null && worker != null) {
			Worker w = parseWorkerFromJson(worker);
			animationPanel.updateWorker(w);
		}
	}

	public void removeWorkerFromWebSocket(String uuid) {
		if (showPanel != null) {
			showPanel.removeWorkerById(uuid);
		}
		if (animationPanel != null) {
			animationPanel.removeWorker(uuid);
		}
	}

	public void clearAllFromWebSocket() {
		if (showPanel != null) {
			showPanel.clearAllWorkers();
		}
		if (animationPanel != null) {
			animationPanel.loadRealWorkers(new ArrayList<>());
		}
	}

	public void syncAllWorkers(JsonNode workers) {
		if (showPanel != null) {
			showPanel.syncAllWorkers(workers);
		}
		if (animationPanel != null && workers != null) {
			List<Worker> workerList = new ArrayList<>();
			for (JsonNode node : workers) {
				Worker w = parseWorkerFromJson(node);
				workerList.add(w);
			}
			animationPanel.loadRealWorkers(workerList);
		}
	}

	private ImageIcon loadImage() {
		try {
			URL imgUrl = getClass().getClassLoader().getResource("images/gear2.png");
			if (imgUrl != null) {
				return new ImageIcon(imgUrl);
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	public boolean isScriptRunning() {
		return readFilePanel != null && readFilePanel.isScriptRunning();
	}

	public void clearAll() {
		helpPanel = null;
		infoPanel = null;
		historyPanel = null;
		firstWorkerPanel = null;
		readFilePanel = null;
		searchByOrganizationPanel = null;
		showPanel = null;
		animationPanel = null;
		if (cardLayout != null && container != null) {
			cardLayout.show(container, "WELCOME");
			currentPanelName = "WELCOME";
		}
	}

	public void stopScriptWithoutConfirm() {
		if (readFilePanel != null && readFilePanel.isScriptRunning()) {
			readFilePanel.stopWithoutConfirm();
		}
	}
}
