package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
	private final String currentUser;
	private final LocaleManager lm;
	private final JTextArea infoTextArea;
	private final JLabel titleLabel;

	public InfoPanel(String username) {
		this.currentUser = username;
		this.lm = LocaleManager.getInstance();
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 70));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
		add(titleLabel, BorderLayout.NORTH);

		infoTextArea = new JTextArea();
		infoTextArea.setBackground(Color.BLACK);
		infoTextArea.setForeground(new Color(200, 200, 200));
		infoTextArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		infoTextArea.setEditable(false);
		infoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		JScrollPane scrollPane = new JScrollPane(infoTextArea);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.BLACK);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		lm.addLocaleChangeListener(() -> {
			updateTitle();
			loadInfo();
		});
		updateTitle();
		loadInfo();
	}

	private void updateTitle() {
		titleLabel.setText(lm.getString("command.info"));
	}

	private void loadInfo() {
		infoTextArea.setText(lm.getString("message.loading_info"));

		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("info").user(currentUser)
						.language(lm.getCurrentLang()).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				SwingUtilities.invokeLater(() -> {
					if (response != null && response.execution()) {
						String info = response.message();
						if (info != null && !info.isEmpty()) {
							String formattedInfo = formatInfoMessage(info);
							infoTextArea.setText(formattedInfo);
						} else if (response.data() != null) {
							infoTextArea.setText(formatInfoMessage(response.data().toString()));
						} else {
							infoTextArea.setText(getDefaultInfoMessage());
						}
					} else {
						String errorMsg = response != null ? response.message() : lm.getString("error.load");
						infoTextArea.setText(lm.getString("error.prefix") + errorMsg);
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(
						() -> infoTextArea.setText(lm.getString("error.connection") + ": " + ex.getMessage()));
			}
		}).start();
	}

	private String formatInfoMessage(String rawInfo) {
		StringBuilder formatted = new StringBuilder();

		formatted.append(lm.getString("window.info")).append(":\n\n");

		String type = extractValue(rawInfo, lm.getString("info.collection_type") + ":");
		if (type == null) {
			type = extractValue(rawInfo, "Тип коллекции:");
		}
		if (type == null) {
			type = extractValue(rawInfo, "Collection type:");
		}
		if (type != null && !type.isEmpty()) {
			formatted.append(lm.getString("info.collection_type")).append(": ").append(type).append("\n");
		}

		String size = extractValue(rawInfo, lm.getString("info.collection_size") + ":");
		if (size == null) {
			size = extractValue(rawInfo, "Количество элементов:");
		}
		if (size == null) {
			size = extractValue(rawInfo, "Number of elements:");
		}
		if (size != null && !size.isEmpty()) {
			formatted.append(lm.getString("info.collection_size")).append(": ").append(size).append("\n");
		}

		String date = extractValue(rawInfo, lm.getString("info.initialization_date") + ":");
		if (date == null) {
			date = extractValue(rawInfo, "Дата инициализации:");
		}
		if (date == null) {
			date = extractValue(rawInfo, "Initialization date:");
		}
		if (date != null && !date.isEmpty()) {
			formatted.append(lm.getString("info.initialization_date")).append(": ").append(date).append("\n");
		}

		if (formatted.length() <= lm.getString("window.info").length() + 2) {
			formatted.append(rawInfo);
		}

		return formatted.toString();
	}

	private String getDefaultInfoMessage() {
		return lm.getString("window.info") + ":\n\n" + lm.getString("info.collection_type") + ": Worker\n"
				+ lm.getString("info.initialization_date") + ": " + new java.util.Date() + "\n"
				+ lm.getString("info.collection_size") + ": " + lm.getString("message.loading");
	}

	private String extractValue(String text, String key) {
		if (text == null || key == null)
			return null;

		int startIndex = text.indexOf(key);
		if (startIndex == -1)
			return null;

		startIndex += key.length();
		while (startIndex < text.length() && text.charAt(startIndex) == ' ') {
			startIndex++;
		}

		int endIndex = startIndex;
		while (endIndex < text.length() && text.charAt(endIndex) != '\n' && text.charAt(endIndex) != '\r') {
			endIndex++;
		}

		if (endIndex > startIndex) {
			return text.substring(startIndex, endIndex).trim();
		}
		return null;
	}
}
