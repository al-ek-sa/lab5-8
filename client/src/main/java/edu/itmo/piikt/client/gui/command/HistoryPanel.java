package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
public class HistoryPanel extends JPanel {
	private MainAppPanel parent;
	private String currentUser;

	public HistoryPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		JLabel titleLabel = new JLabel("ИСТОРИЯ");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 70));
		gbc.insets = new Insets(0, 0, 30, 0);
		add(titleLabel, gbc);

		JLabel historyText = new JLabel("История команд будет отображаться здесь");
		historyText.setForeground(Color.WHITE);
		historyText.setFont(new Font("Arial", Font.PLAIN, 20));
		historyText.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(historyText, gbc);
	}
}
