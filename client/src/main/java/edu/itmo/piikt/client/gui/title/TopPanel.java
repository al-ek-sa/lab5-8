package edu.itmo.piikt.client.gui.title;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {

	public TopPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(0, 50));

		JLabel emptyLabel = new JLabel();
		emptyLabel.setPreferredSize(new Dimension(80, 0));
		add(emptyLabel, BorderLayout.WEST);

		add(new LanguageSelector(), BorderLayout.EAST);
	}
}
