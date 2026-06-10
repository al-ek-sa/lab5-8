package edu.itmo.piikt.client.gui.title;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LeftPanel extends JPanel {
	private Image backgroundImage;

	public LeftPanel() {
		setBackground(Color.BLACK);
		loadImage();
	}

	private void loadImage() {
		try {
			URL imgUrl = getClass().getClassLoader().getResource("images/gear.png");
			if (imgUrl != null) {
				ImageIcon icon = new ImageIcon(imgUrl);
				backgroundImage = icon.getImage();
				repaint();
			}
		} catch (Exception ignored) {
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
