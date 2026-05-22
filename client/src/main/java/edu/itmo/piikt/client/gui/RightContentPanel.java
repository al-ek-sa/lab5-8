package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class RightContentPanel extends JPanel {
	private CardLayout cardLayout;

	public RightContentPanel() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		add(new JPanel(), "REGISTER");
		add(new JPanel(), "MAIN");
	}

	public void showPanel(String panelName) {
		cardLayout.show(this, panelName);
	}
}
