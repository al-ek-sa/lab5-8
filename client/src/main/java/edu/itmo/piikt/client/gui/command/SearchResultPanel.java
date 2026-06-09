package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SearchResultPanel extends JPanel {
	private final String currentUser;
	private final LocaleManager lm;
	private final DefaultTableModel tableModel;
	private final JLabel loadingLabel;
	private final JLabel titleLabel;
	private final JScrollPane scrollPane;

	private final String searchData;

	public SearchResultPanel(String username, String searchData) {
		this.currentUser = username;
		this.searchData = searchData;
		this.lm = LocaleManager.getInstance();

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		String[] columnNames = getLocalizedColumnNames();
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable resultTable = getJTable();

		JTableHeader header = resultTable.getTableHeader();
		header.setBackground(new Color(180, 180, 190));
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Arial", Font.BOLD, 12));
		header.setPreferredSize(new Dimension(header.getWidth(), 30));
		header.setBorder(null);
		header.setReorderingAllowed(true);

		scrollPane = new JScrollPane(resultTable);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.BLACK);

		loadingLabel = new JLabel(lm.getString("message.loading"), SwingConstants.CENTER);
		loadingLabel.setForeground(new Color(100, 200, 100));
		loadingLabel.setFont(new Font("Arial", Font.BOLD, 20));

		add(loadingLabel, BorderLayout.CENTER);

		lm.addLocaleChangeListener(() -> {
			updateTitle();
			updateColumnNames();
		});

		updateTitle();
		loadResults();
	}

	@Nonnull
	private JTable getJTable() {
		JTable resultTable = new JTable(tableModel);
		resultTable.setBackground(new Color(200, 200, 210));
		resultTable.setForeground(Color.BLACK);
		resultTable.setFont(new Font("Arial", Font.PLAIN, 12));
		resultTable.setRowHeight(25);
		resultTable.setGridColor(new Color(180, 180, 190));
		resultTable.setSelectionBackground(new Color(150, 150, 160));
		resultTable.setSelectionForeground(Color.BLACK);
		resultTable.setShowHorizontalLines(true);
		resultTable.setShowVerticalLines(false);
		resultTable.setBorder(null);
		return resultTable;
	}

	private void updateTitle() {
		titleLabel.setText(lm.getString("command.search_organization"));
	}

	private String[] getLocalizedColumnNames() {
		return new String[]{lm.getString("col.id"), lm.getString("col.name"), lm.getString("col.coordinate_x"),
				lm.getString("col.coordinate_y"), lm.getString("col.salary"), lm.getString("col.start_date"),
				lm.getString("col.end_date"), lm.getString("col.status"), lm.getString("col.annual_turnover"),
				lm.getString("col.organization_type"), lm.getString("col.address")};
	}

	private void updateColumnNames() {
		String[] newColumnNames = getLocalizedColumnNames();
		int rowCount = tableModel.getRowCount();
		Object[][] savedData = new Object[rowCount][tableModel.getColumnCount()];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < tableModel.getColumnCount(); j++) {
				savedData[i][j] = tableModel.getValueAt(i, j);
			}
		}

		tableModel.setColumnIdentifiers(newColumnNames);
		tableModel.setRowCount(0);
		for (int i = 0; i < rowCount; i++) {
			tableModel.addRow(savedData[i]);
		}
	}

	private void loadResults() {
		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("count_by_organization").user(currentUser)
						.argumentCommand(searchData).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				SwingUtilities.invokeLater(() -> {
					if (response != null && response.execution()) {
						String result = response.message();

						if (result == null || result.isEmpty() || "EMPTY".equals(result)) {
							loadingLabel.setText(lm.getString("message.no_results"));
							return;
						}

						String[] workers = result.split("###");

						for (String workerStr : workers) {
							Object[] rowData = parseWorkerData(workerStr);
							if (rowData != null) {
								tableModel.addRow(rowData);
							}
						}

						removeAll();
						setLayout(new BorderLayout());
						add(titleLabel, BorderLayout.NORTH);
						add(scrollPane, BorderLayout.CENTER);
						revalidate();
						repaint();

					} else {
						String errorMsg = response != null ? response.message() : lm.getString("error.load");
						loadingLabel.setText(lm.getString("error.prefix") + errorMsg);
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(
						() -> loadingLabel.setText(lm.getString("error.connection") + ": " + ex.getMessage()));
			}
		}).start();
	}

	private Object[] parseWorkerData(String workerStr) {
		Object[] row = new Object[11];

		try {
			row[0] = extractValue(workerStr, "id:");
			row[1] = extractValue(workerStr, "name:");

			String x = extractValue(workerStr, "х:");
			if (x == null)
				x = extractValue(workerStr, "x:");
			row[2] = x != null ? x.replace("coordinate", "").trim() : "";

			String y = extractValue(workerStr, "у:");
			if (y == null)
				y = extractValue(workerStr, "y:");
			row[3] = y != null ? y.replace("coordinate", "").trim() : "";

			row[4] = extractValue(workerStr, "salary:");
			row[5] = extractValue(workerStr, "startDate:");

			String endDate = extractValue(workerStr, "endDate:");
			row[6] = (endDate != null && !endDate.equals("null")) ? endDate : "";

			row[7] = extractValue(workerStr, "status:");

			row[8] = extractValue(workerStr, "annualTurnover:");
			row[9] = extractValue(workerStr, "type:");
			row[10] = extractValue(workerStr, "street:");

			for (int i = 0; i < row.length; i++) {
				if (row[i] == null)
					row[i] = "";
			}
		} catch (Exception e) {
			return null;
		}

		return row;
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

		if (startIndex >= text.length())
			return null;

		int endIndex = startIndex;
		while (endIndex < text.length()) {
			char c = text.charAt(endIndex);
			if (c == ',' || (c == ' ' && endIndex + 1 < text.length()
					&& (text.charAt(endIndex + 1) == 's' || text.charAt(endIndex + 1) == 't'))) {
				break;
			}
			endIndex++;
		}

		String value = text.substring(startIndex, endIndex).trim();
		if (value.endsWith(",")) {
			value = value.substring(0, value.length() - 1);
		}
		return value.isEmpty() ? null : value;
	}
}
