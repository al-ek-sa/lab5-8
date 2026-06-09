package edu.itmo.piikt.client.gui.title;

import edu.itmo.piikt.client.gui.localization.LocaleManager;

import javax.swing.*;
import java.awt.*;

public class LanguageSelector extends JPanel {
	private final JButton langButton;
	private final LocaleManager lm;

	public LanguageSelector() {
		this.lm = LocaleManager.getInstance();
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		setOpaque(false);
		setBackground(Color.BLACK);

		langButton = new JButton(getLangCode());
		langButton.setFont(new Font("Arial", Font.BOLD, 14));
		langButton.setFocusPainted(false);
		langButton.setBackground(new Color(60, 60, 70));
		langButton.setForeground(Color.WHITE);
		langButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
		langButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		langButton.addActionListener(e -> {
			JPopupMenu langMenu = new JPopupMenu();
			langMenu.add(createMenuItem(lm.getString("lang.russian"), "ru", "RU"));
			langMenu.add(createMenuItem(lm.getString("lang.german"), "de", "DE"));
			langMenu.add(createMenuItem(lm.getString("lang.swedish"), "sv", "SV"));
			langMenu.add(createMenuItem(lm.getString("lang.spanish"), "es", "ES"));
			langMenu.show(langButton, 0, langButton.getHeight());
		});

		add(langButton);
		lm.addLocaleChangeListener(() -> SwingUtilities.invokeLater(() -> langButton.setText(getLangCode())));
	}

	private String getLangCode() {
		String lang = lm.getCurrentLang();
		return switch (lang) {
			case "de" -> "DE";
			case "sv" -> "SV";
			case "es" -> "ES";
			default -> "RU";
		};
	}

	private JMenuItem createMenuItem(String langName, String localeCode, String displayCode) {
		JMenuItem item = new JMenuItem(langName + " (" + displayCode + ")");
		item.addActionListener(e -> lm.setLocale(localeCode));
		return item;
	}
}
