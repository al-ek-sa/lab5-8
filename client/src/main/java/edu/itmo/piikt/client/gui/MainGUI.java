package edu.itmo.piikt.client.gui;

import edu.itmo.piikt.client.gui.ss.AppTopPanel;
import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import edu.itmo.piikt.client.gui.title.LeftPanel;
import edu.itmo.piikt.client.gui.title.TopPanel;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
	private JLayeredPane layeredPane;
	private JSplitPane splitPane;
	private TopPanel topPanel;
	private JPanel appPanel;
	private JPanel loginContentPanel;
	private AppTopPanel appTopPanel;

	public MainGUI() {
		setTitle("WORKERFLOW");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

				if (splitPane != null) {
					splitPane.setDividerLocation(0.5);
					splitPane.revalidate();
					splitPane.repaint();
				}

				topPanel.revalidate();
				topPanel.repaint();
				layeredPane.repaint();
			}
		});
	}

	private JPanel createLoginContent() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);

		splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				new LeftPanel(),
				new RightContentPanel(this)
		);
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
		panel.add(appTopPanel, BorderLayout.NORTH);

		panel.add(new MainAppPanel(), BorderLayout.CENTER);

		return panel;
	}

	public void showAppPanel(String username) {
		appTopPanel.setUsername(username);
		loginContentPanel.setVisible(false);
		topPanel.setVisible(false);
		appPanel.setVisible(true);
		layeredPane.repaint();
	}

	public void showLoginPanel() {
		appPanel.setVisible(false);
		loginContentPanel.setVisible(true);
		topPanel.setVisible(true);

		Component[] comps = loginContentPanel.getComponents();
		for (Component comp : comps) {
			if (comp instanceof JSplitPane) {
				JSplitPane sp = (JSplitPane) comp;
				Component right = sp.getRightComponent();
				if (right instanceof RightContentPanel) {
					((RightContentPanel) right).resetToLoginStart();
					break;
				}
			}
		}

		layeredPane.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainGUI().setVisible(true);
		});
	}
}