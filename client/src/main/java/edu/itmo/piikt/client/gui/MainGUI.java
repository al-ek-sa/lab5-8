package edu.itmo.piikt.client.gui;

import com.fasterxml.jackson.databind.JsonNode;
import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.gui.ss.AppTopPanel;
import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import edu.itmo.piikt.client.gui.title.LeftPanel;
import edu.itmo.piikt.client.gui.title.TopPanel;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.client.webSocket.CollectionUpdate;
import edu.itmo.piikt.client.webSocket.Websocket;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.common.models.Status;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {
	private final JLayeredPane layeredPane;
	private JSplitPane splitPane;
	private final TopPanel topPanel;
	private final JPanel appPanel;
	private final JPanel loginContentPanel;
	private AppTopPanel appTopPanel;
	private MainAppPanel mainAppPanel;
	private final LocaleManager lm;
	private Websocket wsClient;

	public MainGUI() {
		this.lm = LocaleManager.getInstance();

		String title = lm.getString("window.title");
		setTitle(title);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(1400, 1000);
		setLocationRelativeTo(null);
		setResizable(true);
		setMinimumSize(new Dimension(800, 600));

		layeredPane = new JLayeredPane();
		layeredPane.setBackground(Color.BLACK);

		loginContentPanel = createLoginContent();
		loginContentPanel.setBounds(0, 0, getWidth(), getHeight());
		layeredPane.add(loginContentPanel, Integer.valueOf(0));

		topPanel = new TopPanel();
		topPanel.setBounds(0, 0, getWidth(), 60);
		topPanel.setOpaque(false);
		layeredPane.add(topPanel, Integer.valueOf(1));

		appPanel = createAppPanel();
		appPanel.setBounds(0, 0, getWidth(), getHeight());
		appPanel.setVisible(false);
		layeredPane.add(appPanel, Integer.valueOf(0));

		setContentPane(layeredPane);

		addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				int w = getWidth();
				int h = getHeight();
				loginContentPanel.setBounds(0, 0, w, h);
				appPanel.setBounds(0, 0, w, h);
				topPanel.setBounds(0, 0, w, 60);
				if (splitPane != null)
					splitPane.setDividerLocation(0.5);
				layeredPane.repaint();
			}
		});
		lm.addLocaleChangeListener(() -> {
			String newTitle = lm.getString("window.title");
			setTitle(newTitle);
		});
	}

	private JPanel createLoginContent() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);

		RightContentPanel rightContentPanel = new RightContentPanel(this);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new LeftPanel(), rightContentPanel);
		splitPane.setDividerLocation(0.5);
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0.5);
		splitPane.setBackground(Color.BLACK);
		splitPane.setBorder(null);
		splitPane.setDividerSize(0);

		panel.add(splitPane, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createAppPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);

		appTopPanel = new AppTopPanel(this);
		mainAppPanel = new MainAppPanel();
		appTopPanel.setMainAppPanel(mainAppPanel);

		panel.add(appTopPanel, BorderLayout.NORTH);
		panel.add(mainAppPanel, BorderLayout.CENTER);

		return panel;
	}

	public void showAppPanel(String username) {
		try {
			Network network = new Network();
			network.connect();

			GuiCommandSender.INSTANCE.setNetwork(network);
			GuiCommandSender.INSTANCE.setUser(username);

			appTopPanel.setUsername(username);
			loginContentPanel.setVisible(false);
			topPanel.setVisible(false);
			appPanel.setVisible(true);
			layeredPane.repaint();
			initWebSocket();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, lm.getString("error.connection") + ": " + e.getMessage(),
					lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void logout() {
		if (mainAppPanel != null && mainAppPanel.isScriptRunning()) {
			int confirm = JOptionPane.showConfirmDialog(this, lm.getString("confirm.logout_while_script"),
					lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
			mainAppPanel.stopScriptWithoutConfirm();
		}

		if (wsClient != null) {
			wsClient.disconnect();
			wsClient = null;
		}

		if (mainAppPanel != null) {
			mainAppPanel.clearAll();
		}

		GuiCommandSender.INSTANCE.setUser(null);
		GuiCommandSender.INSTANCE.setNetwork(null);

		clearAllTextFields(loginContentPanel);

		appPanel.setVisible(false);
		topPanel.setVisible(true);
		loginContentPanel.setVisible(true);

		if (splitPane != null) {
			splitPane.setDividerLocation(0.5);
		}

		layeredPane.repaint();
	}

	private void clearAllTextFields(Container container) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText("");
			} else if (comp instanceof Container) {
				clearAllTextFields((Container) comp);
			}
		}
	}

	private void initWebSocket() {
		if (wsClient == null) {
			wsClient = new Websocket("localhost", 7083, this::onCollectionUpdate, this::onConnectionChange);
			wsClient.connect();
		}
	}

	private List<Worker> parseWorkersFromJson(JsonNode workersNode) {
		List<Worker> workers = new ArrayList<>();
		if (workersNode == null || !workersNode.isArray()) {
			return workers;
		}
		for (JsonNode node : workersNode) {
			Worker worker = new Worker();
			if (node.has("uuid")) {
				worker.setUuid(node.get("uuid").asText());
			}
			if (node.has("name")) {
				worker.setName(node.get("name").asText());
			}
			if (node.has("salary")) {
				worker.setSalary(node.get("salary").floatValue());
			}
			if (node.has("coordinates") && !node.get("coordinates").isNull()) {
				JsonNode coords = node.get("coordinates");
				Coordinates c = new Coordinates();
				if (coords.has("x")) {
					c.setX(coords.get("x").asLong());
				}
				if (coords.has("y")) {
					c.setY((float) coords.get("y").asDouble());
				}
				worker.setCoordinates(c);
			}
			if (node.has("status") && !node.get("status").isNull()) {
				try {
					worker.setStatus(Status.valueOf(node.get("status").asText()));
				} catch (IllegalArgumentException e) {
					// ignore
				}
			}
			workers.add(worker);
		}
		return workers;
	}

	private void onCollectionUpdate(CollectionUpdate update) {
		SwingUtilities.invokeLater(() -> {
			switch (update) {
				case CollectionUpdate.Add add -> mainAppPanel.addWorkerFromWebSocket(add.worker());
				case CollectionUpdate.Update upd -> mainAppPanel.updateWorkerFromWebSocket(upd.worker());
				case CollectionUpdate.Remove rem -> mainAppPanel.removeWorkerFromWebSocket(rem.id());
				case CollectionUpdate.Clear clear -> mainAppPanel.clearAllFromWebSocket();
				case CollectionUpdate.FullSync fullSync -> {
					List<Worker> workers = parseWorkersFromJson(fullSync.workers());
					mainAppPanel.syncAllWorkers(fullSync.workers());
					mainAppPanel.updateAnimationWorkers(workers);
				}
				default -> {
				}
			}
		});
	}

	private void onConnectionChange(boolean connected) {
		// todo
	}

	@Override
	protected void processWindowEvent(java.awt.event.WindowEvent e) {
		if (e.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
			if (mainAppPanel != null && mainAppPanel.isScriptRunning()) {
				int confirm = JOptionPane.showConfirmDialog(this, lm.getString("confirm.close_while_script"),
						lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					mainAppPanel.stopScriptWithoutConfirm();
					if (wsClient != null) {
						wsClient.disconnect();
					}
					dispose();
				}
				return;
			}

			if (wsClient != null) {
				wsClient.disconnect();
			}
			dispose();
		} else {
			super.processWindowEvent(e);
		}
	}
}
