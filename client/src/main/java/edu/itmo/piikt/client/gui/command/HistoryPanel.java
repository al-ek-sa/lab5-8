package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.command.history.HistoryCommands;
import edu.itmo.piikt.client.gui.localization.LocaleManager;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class HistoryPanel extends JPanel {
	private final LocaleManager lm;
	private final JTextArea historyTextArea;
	private final JLabel titleLabel;

	public HistoryPanel() {
		this.lm = LocaleManager.getInstance();

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
		add(titleLabel, BorderLayout.NORTH);

		historyTextArea = new JTextArea();
		historyTextArea.setBackground(Color.BLACK);
		historyTextArea.setForeground(new Color(200, 200, 200));
		historyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		historyTextArea.setEditable(false);
		historyTextArea.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

		JScrollPane scrollPane = new JScrollPane(historyTextArea);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.BLACK);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		lm.addLocaleChangeListener(() -> {
			updateTitle();
			loadHistory();
		});
		updateTitle();
		loadHistory();
	}

	private void updateTitle() {
		titleLabel.setText(lm.getString("command.history"));
	}

	private void loadHistory() {
		SwingUtilities.invokeLater(() -> {
			LinkedList<String> history = HistoryCommands.INSTANCE.getLinkedList();

			if (history == null || history.isEmpty()) {
				historyTextArea.setText(lm.getString("message.history_empty"));
				return;
			}

			LinkedHashSet<String> uniqueCommands = new LinkedHashSet<>(history);

			StringBuilder formattedHistory = new StringBuilder();

			int counter = 1;
			for (String command : uniqueCommands) {
				String translatedCommand = translateCommand(command);
				formattedHistory.append(counter).append(". ").append(translatedCommand).append("\n");
				counter++;
			}

			historyTextArea.setText(formattedHistory.toString());
		});
	}
	private String translateCommand(String command) {
		return switch (command) {
			case "help" -> lm.getString("command.help");
			case "info" -> lm.getString("command.info");
			case "show" -> lm.getString("command.show");
			case "add" -> lm.getString("command.add");
			case "update" -> lm.getString("command.update");
			case "remove_by_id" -> lm.getString("command.remove");
			case "clear" -> lm.getString("command.clear");
			case "read_file" -> lm.getString("command.read_file");
			case "head" -> lm.getString("command.first_worker");
			case "remove_lower" -> lm.getString("button.delete_by_date");
			case "history" -> lm.getString("command.history");
			case "animation" -> lm.getString("command.animation");
			case "search_by_organization" -> lm.getString("command.search_organization");
			default -> command;
		};
	}
}
