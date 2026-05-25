package edu.itmo.piikt.client.gui.title;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LeftPanel extends JPanel {
	private Image backgroundImage;

	public LeftPanel() {
		setBackground(Color.BLACK);
		loadImage("images/gear.png");
	}

	private void loadImage(String path) {
		try {
			URL imgUrl = getClass().getClassLoader().getResource(path);
			if (imgUrl != null) {
				ImageIcon icon = new ImageIcon(imgUrl);
				backgroundImage = icon.getImage();
				repaint();
			}
		} catch (Exception e) {
			System.err.println("Image not found: " + path);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			int panelHeight = getHeight();
			g.drawImage(backgroundImage, 0, 0, panelHeight / 2 + 15, panelHeight, this);
		} else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
