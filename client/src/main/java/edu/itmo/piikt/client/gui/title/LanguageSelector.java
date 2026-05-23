package edu.itmo.piikt.client.gui.title;

import javax.swing.*;
import java.awt.*;

public class LanguageSelector extends JPanel {
	private final JButton langButton;

	public LanguageSelector() {
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		setOpaque(false);
		setBackground(Color.BLACK);

		langButton = new JButton("RU");
		langButton.setFont(new Font("Arial", Font.BOLD, 14));
		langButton.setFocusPainted(false);
		langButton.setBackground(new Color(60, 60, 70));
		langButton.setForeground(Color.WHITE);
		langButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
		langButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPopupMenu langMenu = new JPopupMenu();
		langMenu.add(createMenuItem("Русский", "RU"));
		langMenu.add(createMenuItem("Deutsch", "DE"));
		langMenu.add(createMenuItem("Svenska", "SV"));
		langMenu.add(createMenuItem("Español", "ES"));

		langButton.addActionListener(e -> langMenu.show(langButton, 0, langButton.getHeight()));

		add(langButton);
	}

	private JMenuItem createMenuItem(String langName, String code) {
		JMenuItem item = new JMenuItem(langName + " (" + code + ")");
		item.addActionListener(e -> {
			System.out.println("Language switched to: " + langName);
			langButton.setText(code);
		});
		return item;
	}
}
