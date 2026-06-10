package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FirstWorkerPanel extends JPanel {
	private final String currentUser;
	private final LocaleManager lm;
	private final JTextArea firstWorkerTextArea;
	private final JLabel titleLabel;

	public FirstWorkerPanel(String username) {
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

		firstWorkerTextArea = new JTextArea();
		firstWorkerTextArea.setBackground(Color.BLACK);
		firstWorkerTextArea.setForeground(new Color(200, 200, 200));
		firstWorkerTextArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		firstWorkerTextArea.setEditable(false);
		firstWorkerTextArea.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

		JScrollPane scrollPane = new JScrollPane(firstWorkerTextArea);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.BLACK);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		lm.addLocaleChangeListener(() -> {
			updateTitle();
			loadFirstWorker();
		});

		updateTitle();
		loadFirstWorker();
	}

	private void updateTitle() {
		titleLabel.setText(lm.getString("command.first_worker"));
	}

	private void loadFirstWorker() {
		firstWorkerTextArea.setText(lm.getString("message.loading"));

		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("head").user(currentUser)
						.language(lm.getCurrentLang()).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				SwingUtilities.invokeLater(() -> {
					if (response != null && response.execution()) {
						String rawData = "";

						if (response.data() instanceof List) {
							List<String> data = response.data();
							if (!data.isEmpty()) {
								rawData = data.getFirst();
							}
						} else if (response.message() != null) {
							rawData = response.message();
						}

						if (rawData == null || rawData.isEmpty()
								|| rawData.contains(lm.getString("message.collection_empty"))
								|| rawData.contains("COLLECTION IS EMPTY") || rawData.contains("empty")) {
							firstWorkerTextArea.setText(lm.getString("message.collection_empty") + "\n\n"
									+ lm.getString("message.no_workers"));
						} else {
							String formattedData = formatWorkerData(rawData);
							firstWorkerTextArea.setText(formattedData);
						}
					} else {
						String errorMsg = response != null ? response.message() : lm.getString("error.load");
						firstWorkerTextArea.setText(lm.getString("error.prefix") + errorMsg);
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(
						() -> firstWorkerTextArea.setText(lm.getString("error.connection") + ": " + ex.getMessage()));
			}
		}).start();
	}

	private String formatWorkerData(String rawData) {
		StringBuilder formatted = new StringBuilder();

		String id = extractValue(rawData, "id:");
		if (id != null && !id.isEmpty()) {
			formatted.append(lm.getString("col.id")).append(": ").append(id).append("\n\n");
		} else {
			return lm.getString("message.no_data");
		}

		String name = extractValue(rawData, "name:");
		if (name != null && !name.isEmpty()) {
			formatted.append(lm.getString("col.name")).append(": ").append(name).append("\n\n");
		}

		String x = extractValue(rawData, "х:");
		if (x == null)
			x = extractValue(rawData, "x:");
		String y = extractValue(rawData, "у:");
		if (y == null)
			y = extractValue(rawData, "y:");

		if (x != null && y != null && !x.isEmpty() && !y.isEmpty()) {
			formatted.append(lm.getString("col.coordinate_x")).append("=").append(x).append(", ")
					.append(lm.getString("col.coordinate_y")).append("=").append(y).append("\n\n");
		}

		String salary = extractValue(rawData, "salary:");
		if (salary != null && !salary.isEmpty()) {
			formatted.append(lm.getString("col.salary")).append(": ").append(salary).append("\n\n");
		}

		String startDate = extractValue(rawData, "startDate:");
		if (startDate != null && !startDate.isEmpty() && !startDate.equals("null")) {
			formatted.append(lm.getString("col.start_date")).append(": ").append(formatDate(startDate)).append("\n\n");
		}

		String endDate = extractValue(rawData, "endDate:");
		if (endDate != null && !endDate.isEmpty() && !endDate.equals("null")) {
			formatted.append(lm.getString("col.end_date")).append(": ").append(formatDate(endDate)).append("\n\n");
		}

		String status = extractValue(rawData, "status:");
		if (status != null && !status.isEmpty()) {
			formatted.append(lm.getString("col.status")).append(": ").append(status).append("\n\n");
		}

		int orgIndex = rawData.indexOf("organization:");
		if (orgIndex != -1) {
			String orgPart = rawData.substring(orgIndex);
			formatted.append(lm.getString("form.organization_type")).append(":\n\n");

			String turnover = extractValue(orgPart, "annualTurnover:");
			if (turnover != null && !turnover.isEmpty()) {
				formatted.append("  ").append(lm.getString("col.annual_turnover")).append(": ").append(turnover)
						.append("\n\n");
			}

			String type = extractValue(orgPart, "type:");
			if (type != null && !type.isEmpty()) {
				formatted.append("  ").append(lm.getString("col.organization_type")).append(": ").append(type)
						.append("\n\n");
			}

			String street = extractValue(orgPart, "street:");
			if (street != null && !street.isEmpty()) {
				formatted.append("  ").append(lm.getString("col.address")).append(": ").append(street).append("\n\n");
			}
		}

		return formatted.toString();
	}

	private String formatDate(String dateStr) {
		if (dateStr.startsWith("[") && dateStr.endsWith("]")) {
			String[] parts = dateStr.substring(1, dateStr.length() - 1).split(",");
			if (parts.length >= 3) {
				String year = parts[0].trim();
				String month = parts[1].trim();
				String day = parts[2].trim();
				if (month.length() == 1)
					month = "0" + month;
				if (day.length() == 1)
					day = "0" + day;
				return year + "-" + month + "-" + day;
			}
		}
		return dateStr;
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

		if (startIndex < text.length() && text.charAt(startIndex) == '"') {
			startIndex++;
			int endIndex = startIndex;
			while (endIndex < text.length() && text.charAt(endIndex) != '"') {
				endIndex++;
			}
			return text.substring(startIndex, endIndex).trim();
		}

		int endIndex = startIndex;
		while (endIndex < text.length()) {
			char c = text.charAt(endIndex);
			if (c == ',' || c == '}' || c == ']' || (c == ' ' && endIndex + 1 < text.length()
					&& (text.charAt(endIndex + 1) == 's' || text.charAt(endIndex + 1) == 't'))) {
				break;
			}
			endIndex++;
		}

		String value = text.substring(startIndex, endIndex).trim();
		if (value.endsWith(",")) {
			value = value.substring(0, value.length() - 1);
		}
		if (value.endsWith("}")) {
			value = value.substring(0, value.length() - 1);
		}
		if (value.endsWith("]")) {
			value = value.substring(0, value.length() - 1);
		}
		return value.isEmpty() ? null : value;
	}
}
