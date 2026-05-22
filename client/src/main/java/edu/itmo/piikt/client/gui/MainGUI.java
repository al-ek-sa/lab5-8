package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

	public MainGUI() {
		setTitle("WORKERFLOW");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setResizable(true);
		setMinimumSize(new Dimension(800, 600));
		JPanel mainContainer = new JPanel(new BorderLayout());
		mainContainer.add(new TopPanel(), BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new ShapesPanel(), new RightContentPanel());
		splitPane.setResizeWeight(0.35);
		splitPane.setDividerLocation(400);
		splitPane.setEnabled(false);
		mainContainer.add(splitPane, BorderLayout.CENTER);
		setContentPane(mainContainer);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainGUI().setVisible(true);
		});
	}
}
