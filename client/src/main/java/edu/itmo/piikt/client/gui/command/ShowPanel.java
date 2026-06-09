package edu.itmo.piikt.client.gui.command;

import com.fasterxml.jackson.databind.JsonNode;
import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.common.models.Status;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ShowPanel extends JPanel {
	private final MainAppPanel parent;
	private final String currentUser;
	private final LocaleManager lm;
	private JTable resultTable;
	private DefaultTableModel tableModel;
	private JComboBox<String> sortColumnComboBox;
	private JLabel titleLabel;
	private JLabel sortLabel;
	private JButton applySortButton;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton deleteByDateButton;
	private JButton clearButton;
	private List<Worker> workersCache = new ArrayList<>();
	private List<Object[]> cachedData = new ArrayList<>();

	public ShowPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		this.lm = LocaleManager.getInstance();

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(titleLabel, BorderLayout.NORTH);

		JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		sortPanel.setOpaque(false);

		sortLabel = new JLabel();
		sortLabel.setForeground(Color.WHITE);
		sortLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		sortPanel.add(sortLabel);

		sortColumnComboBox = new JComboBox<>();
		sortColumnComboBox.setBackground(new Color(48, 48, 48));
		sortColumnComboBox.setForeground(Color.WHITE);
		sortColumnComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		sortColumnComboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		sortPanel.add(sortColumnComboBox);

		applySortButton = new JButton();
		applySortButton.setBackground(new Color(48, 48, 48));
		applySortButton.setForeground(Color.WHITE);
		applySortButton.setFont(new Font("Arial", Font.BOLD, 14));
		applySortButton.setFocusPainted(false);
		applySortButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		applySortButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		applySortButton.addActionListener(e -> applySortStream());
		sortPanel.add(applySortButton);

		add(sortPanel, BorderLayout.NORTH);

		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		resultTable = new JTable(tableModel);
		resultTable.setBackground(new Color(200, 200, 210));
		resultTable.setForeground(Color.BLACK);
		resultTable.setFont(new Font("Arial", Font.PLAIN, 14));
		resultTable.setRowHeight(30);
		resultTable.setGridColor(new Color(180, 180, 190));
		resultTable.setSelectionBackground(new Color(150, 150, 160));
		resultTable.setSelectionForeground(Color.BLACK);
		resultTable.setShowHorizontalLines(true);
		resultTable.setShowVerticalLines(false);
		resultTable.setBorder(null);
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header = resultTable.getTableHeader();
		header.setBackground(new Color(180, 180, 190));
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Arial", Font.BOLD, 14));
		header.setPreferredSize(new Dimension(header.getWidth(), 35));
		header.setBorder(null);
		header.setReorderingAllowed(true);

		JScrollPane scrollPane = new JScrollPane(resultTable);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.BLACK);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridy = 0;

		addButton = new JButton();
		styleButton(addButton);
		addButton.addActionListener(e -> onAdd());
		buttonPanel.add(addButton, gbc);

		editButton = new JButton();
		styleButton(editButton);
		editButton.addActionListener(e -> onEdit());
		buttonPanel.add(editButton, gbc);

		deleteButton = new JButton();
		styleButton(deleteButton);
		deleteButton.addActionListener(e -> onDelete());
		buttonPanel.add(deleteButton, gbc);

		deleteByDateButton = new JButton();
		styleButton(deleteByDateButton);
		deleteByDateButton.addActionListener(e -> onDeleteByDate());
		buttonPanel.add(deleteByDateButton, gbc);

		clearButton = new JButton();
		styleButton(clearButton);
		clearButton.addActionListener(e -> onClear());
		buttonPanel.add(clearButton, gbc);

		add(buttonPanel, BorderLayout.SOUTH);
		lm.addLocaleChangeListener(() -> {
			updateTexts();
			updateTableHeaders();
		});
		updateTexts();
		loadData();
	}

	private void updateTexts() {
		titleLabel.setText(lm.getString("command.show"));
		sortLabel.setText(lm.getString("table.sort_by") + ":");
		applySortButton.setText(lm.getString("button.apply"));
		addButton.setText(lm.getString("command.add"));
		editButton.setText(lm.getString("command.update"));
		deleteButton.setText(lm.getString("command.remove"));
		deleteByDateButton.setText(lm.getString("button.delete_by_date"));
		clearButton.setText(lm.getString("command.clear"));
	}

	private void updateTableHeaders() {
		String[] columnNames = getLocalizedColumnNames();
		int rowCount = tableModel.getRowCount();
		Object[][] savedData = new Object[rowCount][tableModel.getColumnCount()];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < tableModel.getColumnCount(); j++) {
				savedData[i][j] = tableModel.getValueAt(i, j);
			}
		}
		tableModel.setColumnIdentifiers(columnNames);
		tableModel.setRowCount(0);
		for (int i = 0; i < rowCount; i++) {
			tableModel.addRow(savedData[i]);
		}
		sortColumnComboBox.removeAllItems();
		for (String colName : columnNames) {
			sortColumnComboBox.addItem(colName);
		}
		revalidate();
		repaint();
	}

	private String[] getLocalizedColumnNames() {
		return new String[]{lm.getString("col.id"), lm.getString("col.name"), lm.getString("col.coordinate_x"),
				lm.getString("col.coordinate_y"), lm.getString("col.salary"), lm.getString("col.start_date"),
				lm.getString("col.end_date"), lm.getString("col.status"), lm.getString("col.annual_turnover"),
				lm.getString("col.organization_type"), lm.getString("col.address")};
	}

	private void styleButton(JButton button) {
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(180, 55));
	}

	private void loadData() {
		tableModel.setRowCount(0);
		cachedData.clear();
		workersCache.clear();

		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("show").user(currentUser).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				SwingUtilities.invokeLater(() -> {
					if (response != null && response.execution()) {
						if (response.message() != null && response.message().contains("EMPTY")) {
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("message.collection_empty"),
									lm.getString("message.info"), JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						List<String> workers = null;
						if (response.data() instanceof List) {
							workers = (List<String>) response.data();
						}

						if (workers == null || workers.isEmpty()) {
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("message.no_data"),
									lm.getString("message.info"), JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						String[] columnNames = getLocalizedColumnNames();
						tableModel.setColumnIdentifiers(columnNames);
						sortColumnComboBox.removeAllItems();
						for (String colName : columnNames) {
							sortColumnComboBox.addItem(colName);
						}
						for (String workerStr : workers) {
							Worker worker = parseWorkerFromString(workerStr);
							if (worker != null) {
								workersCache.add(worker);
								Object[] rowData = workerToRow(worker);
								tableModel.addRow(rowData);
								cachedData.add(rowData);
							}
						}
					} else {
						String errorMsg = response != null ? response.message() : lm.getString("error.load");
						JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("error.prefix") + errorMsg,
								lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(ShowPanel.this,
							lm.getString("error.connection") + ": " + ex.getMessage(), lm.getString("message.error"),
							JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	private Worker parseWorkerFromString(String workerStr) {
		try {
			Worker worker = new Worker();
			worker.setUuid(extractValue(workerStr, "id:"));
			worker.setName(extractValue(workerStr, "name:"));
			String x = extractValue(workerStr, "х:");
			if (x == null)
				x = extractValue(workerStr, "x:");
			String y = extractValue(workerStr, "у:");
			if (y == null)
				y = extractValue(workerStr, "y:");
			if (x != null && y != null) {
				Coordinates coords = new Coordinates();
				coords.setX(Long.parseLong(x));
				coords.setY(Float.parseFloat(y));
				worker.setCoordinates(coords);
			}
			String salaryStr = extractValue(workerStr, "salary:");
			if (salaryStr != null && !salaryStr.isEmpty()) {
				worker.setSalary(Float.parseFloat(salaryStr));
			}

			String startDateStr = extractValue(workerStr, "startDate:");
			if (startDateStr != null && !startDateStr.isEmpty() && !"null".equals(startDateStr)) {
				try {
					if (startDateStr.startsWith("[") && startDateStr.endsWith("]")) {
						String[] parts = startDateStr.substring(1, startDateStr.length() - 1).split(",");
						if (parts.length >= 3) {
							int year = Integer.parseInt(parts[0].trim());
							int month = Integer.parseInt(parts[1].trim());
							int day = Integer.parseInt(parts[2].trim());
							worker.setStartDate(LocalDate.of(year, month, day));
						}
					} else {
						worker.setStartDate(LocalDate.parse(startDateStr));
					}
				} catch (Exception e) {
					// ignore
				}
			}

			String endDateStr = extractValue(workerStr, "endDate:");
			if (endDateStr != null && !endDateStr.isEmpty() && !"null".equals(endDateStr)) {
				try {
					if (endDateStr.startsWith("[") && endDateStr.endsWith("]")) {
						String[] parts = endDateStr.substring(1, endDateStr.length() - 1).split(",");
						if (parts.length >= 3) {
							int year = Integer.parseInt(parts[0].trim());
							int month = Integer.parseInt(parts[1].trim());
							int day = Integer.parseInt(parts[2].trim());
							worker.setEndDate(LocalDate.of(year, month, day));
						}
					} else {
						worker.setEndDate(LocalDate.parse(endDateStr));
					}
				} catch (Exception e) {
					// ignore
				}
			}

			String statusStr = extractValue(workerStr, "status:");
			if (statusStr != null && !statusStr.isEmpty()) {
				try {
					worker.setStatus(Status.valueOf(statusStr));
				} catch (IllegalArgumentException e) {
				}
			}

			Organization org = new Organization();
			boolean hasOrg = false;

			String turnoverStr = extractValue(workerStr, "annualTurnover:");
			if (turnoverStr != null && !turnoverStr.isEmpty()) {
				org.setAnnualTurnover(Integer.parseInt(turnoverStr));
				hasOrg = true;
			}

			String typeStr = extractValue(workerStr, "type:");
			if (typeStr != null && !typeStr.isEmpty()) {
				try {
					org.setType(OrganizationType.valueOf(typeStr));
					hasOrg = true;
				} catch (IllegalArgumentException e) {
				}
			}

			String streetStr = extractValue(workerStr, "street:");
			if (streetStr != null && !streetStr.isEmpty()) {
				Address address = new Address();
				address.setStreet(streetStr);
				org.setOfficialAddress(address);
				hasOrg = true;
			}

			if (hasOrg) {
				worker.setOrganization(org);
			}
			return worker;
		} catch (Exception e) {
			return null;
		}
	}
	private Object[] workerToRow(Worker worker) {
		Object[] row = new Object[11];
		row[0] = worker.getUuid();
		row[1] = worker.getName();
		if (worker.getCoordinates() != null) {
			row[2] = worker.getCoordinates().getX();
			row[3] = worker.getCoordinates().getY();
		} else {
			row[2] = "";
			row[3] = "";
		}
		row[4] = worker.getSalary();
		row[5] = worker.getStartDate() != null ? worker.getStartDate().toString() : "";
		row[6] = worker.getEndDate() != null ? worker.getEndDate().toString() : "";
		row[7] = worker.getStatus() != null ? worker.getStatus().toString() : "";
		if (worker.getOrganization() != null) {
			row[8] = worker.getOrganization().getAnnualTurnover();
			row[9] = worker.getOrganization().getType() != null ? worker.getOrganization().getType().toString() : "";
			if (worker.getOrganization().getOfficialAddress() != null) {
				row[10] = worker.getOrganization().getOfficialAddress().getStreet();
			} else {
				row[10] = "";
			}
		} else {
			row[8] = "";
			row[9] = "";
			row[10] = "";
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
		while (startIndex < text.length() && text.charAt(startIndex) == ' ')
			startIndex++;
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
		if (value.endsWith(","))
			value = value.substring(0, value.length() - 1);
		return value.isEmpty() ? null : value;
	}

	private void applySortStream() {
		int columnIndex = sortColumnComboBox.getSelectedIndex();
		if (columnIndex == -1 || workersCache.isEmpty())
			return;

		List<Worker> sortedWorkers;

		switch (columnIndex) {
			case 0 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(Worker::getUuid, Comparator.nullsLast(String::compareTo)))
						.collect(Collectors.toList());
				break;
			case 1 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(Worker::getName, Comparator.nullsLast(String::compareTo)))
						.collect(Collectors.toList());
				break;
			case 2 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(
								w -> w.getCoordinates() != null ? w.getCoordinates().getX() : Long.MAX_VALUE,
								Long::compareTo))
						.collect(Collectors.toList());
				break;
			case 3 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(
								w -> w.getCoordinates() != null ? (double) w.getCoordinates().getY() : Double.MAX_VALUE,
								Double::compareTo))
						.collect(Collectors.toList());
				break;
			case 4 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(Worker::getSalary, Comparator.nullsLast(Float::compareTo)))
						.collect(Collectors.toList());
				break;
			case 5 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(Worker::getStartDate, Comparator.nullsLast(LocalDate::compareTo)))
						.collect(Collectors.toList());
				break;
			case 6 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator.comparing(Worker::getEndDate, Comparator.nullsLast(LocalDate::compareTo)))
						.collect(Collectors.toList());
				break;
			case 7 :
				sortedWorkers = workersCache.stream().sorted(
						Comparator.comparing(w -> w.getStatus() != null ? w.getStatus().name() : "", String::compareTo))
						.collect(Collectors.toList());
				break;
			case 8 :
				sortedWorkers = workersCache.stream().sorted(Comparator.comparing(
						w -> w.getOrganization() != null ? w.getOrganization().getAnnualTurnover() : Integer.MAX_VALUE,
						Integer::compareTo)).collect(Collectors.toList());
				break;
			case 9 :
				sortedWorkers = workersCache.stream()
						.sorted(Comparator
								.comparing(w -> w.getOrganization() != null && w.getOrganization().getType() != null
										? w.getOrganization().getType().name()
										: "", String::compareTo))
						.collect(Collectors.toList());
				break;
			case 10 :
				sortedWorkers = workersCache.stream().sorted(Comparator.comparing(w -> {
					if (w.getOrganization() != null && w.getOrganization().getOfficialAddress() != null) {
						return w.getOrganization().getOfficialAddress().getStreet();
					}
					return "";
				}, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
				break;
			default :
				sortedWorkers = new ArrayList<>(workersCache);
		}

		tableModel.setRowCount(0);
		cachedData.clear();
		for (Worker worker : sortedWorkers) {
			Object[] rowData = workerToRow(worker);
			tableModel.addRow(rowData);
			cachedData.add(rowData);
		}
	}

	private void onAdd() {
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		WorkerFormDialog dialog = new WorkerFormDialog(frame, parent, currentUser);
		dialog.setVisible(true);
	}

	private void onEdit() {
		int selectedRow = resultTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, lm.getString("error.select_row_to_edit"), lm.getString("message.error"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		String workerId = (String) tableModel.getValueAt(selectedRow, 0);
		if (workerId == null || workerId.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.cannot_get_id"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		Object[] rowData = new Object[tableModel.getColumnCount()];
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			rowData[i] = tableModel.getValueAt(selectedRow, i);
		}
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		WorkerFormDialog dialog = new WorkerFormDialog(frame, parent, currentUser, true, workerId, rowData);
		dialog.setVisible(true);
	}

	private void onDelete() {
		int selectedRow = resultTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, lm.getString("error.select_row_to_delete"),
					lm.getString("message.error"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		String workerId = (String) tableModel.getValueAt(selectedRow, 0);
		if (workerId == null || workerId.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.cannot_get_id"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, lm.getString("confirm.delete_worker") + " " + workerId + "?",
				lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			setEnabled(false);
			new Thread(() -> {
				try {
					ClientCommand command = ClientCommand.builder().nameCommand("remove_by_id").user(currentUser)
							.argumentCommand(workerId).build();
					ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						if (response != null && response.execution()) {
							tableModel.removeRow(selectedRow);
							workersCache.removeIf(w -> w.getUuid().equals(workerId));
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("message.delete_success"),
									lm.getString("message.success"), JOptionPane.INFORMATION_MESSAGE);
						} else {
							String errorMsg = response != null ? response.message() : lm.getString("error.delete");
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("error.prefix") + errorMsg,
									lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						JOptionPane.showMessageDialog(ShowPanel.this,
								lm.getString("error.connection") + ": " + ex.getMessage(),
								lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
					});
				}
			}).start();
		}
	}

	private void onDeleteByDate() {
		String dateStr = JOptionPane.showInputDialog(this, lm.getString("delete.date_prompt"),
				lm.getString("button.delete_by_date"), JOptionPane.QUESTION_MESSAGE);
		if (dateStr == null || dateStr.trim().isEmpty())
			return;
		if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
			JOptionPane.showMessageDialog(this, lm.getString("error.invalid_date_format"),
					lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, lm.getString("confirm.delete_by_date") + " " + dateStr + "?",
				lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			setEnabled(false);
			new Thread(() -> {
				try {
					ClientCommand command = ClientCommand.builder().nameCommand("remove_lower").user(currentUser)
							.argumentCommand(dateStr).build();
					ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						if (response != null && response.execution()) {
							loadData();
							JOptionPane.showMessageDialog(ShowPanel.this,
									response.message() != null
											? response.message()
											: lm.getString("message.delete_success"),
									lm.getString("message.success"), JOptionPane.INFORMATION_MESSAGE);
						} else {
							String errorMsg = response != null ? response.message() : lm.getString("error.delete");
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("error.prefix") + errorMsg,
									lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						JOptionPane.showMessageDialog(ShowPanel.this,
								lm.getString("error.connection") + ": " + ex.getMessage(),
								lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
					});
				}
			}).start();
		}
	}

	private void onClear() {
		int confirm = JOptionPane.showConfirmDialog(this, lm.getString("confirm.clear_all"),
				lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			setEnabled(false);
			new Thread(() -> {
				try {
					ClientCommand command = ClientCommand.builder().nameCommand("clear").user(currentUser).build();
					ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						if (response != null && response.execution()) {
							loadData();
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("message.clear_success"),
									lm.getString("message.success"), JOptionPane.INFORMATION_MESSAGE);
						} else {
							String errorMsg = response != null ? response.message() : lm.getString("error.clear");
							JOptionPane.showMessageDialog(ShowPanel.this, lm.getString("error.prefix") + errorMsg,
									lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						JOptionPane.showMessageDialog(ShowPanel.this,
								lm.getString("error.connection") + ": " + ex.getMessage(),
								lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
					});
				}
			}).start();
		}
	}

	public void syncAllWorkers(JsonNode workers) {
		workersCache.clear();
		SwingUtilities.invokeLater(() -> {
			tableModel.setRowCount(0);
			cachedData.clear();
			for (JsonNode node : workers) {
				Worker w = parseWorkerFromJsonToWorker(node);
				if (w != null) {
					workersCache.add(w);
					Object[] row = workerToRow(w);
					tableModel.addRow(row);
					cachedData.add(row);
				}
			}
		});
	}

	public void addWorkerFromJson(JsonNode worker) {
		Worker w = parseWorkerFromJsonToWorker(worker);
		if (w != null) {
			workersCache.add(w);
			Object[] row = workerToRow(w);
			SwingUtilities.invokeLater(() -> {
				tableModel.addRow(row);
				cachedData.add(row);
			});
		}
	}

	public void updateWorkerFromJson(JsonNode worker) {
		String uuid = worker.get("uuid").asText();
		Worker updated = parseWorkerFromJsonToWorker(worker);

		for (int i = 0; i < workersCache.size(); i++) {
			if (workersCache.get(i).getUuid().equals(uuid)) {
				workersCache.set(i, updated);
				break;
			}
		}

		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if (tableModel.getValueAt(i, 0).equals(uuid)) {
					Object[] newRow = workerToRow(updated);
					for (int j = 0; j < newRow.length; j++) {
						tableModel.setValueAt(newRow[j], i, j);
					}
					if (i < cachedData.size()) {
						cachedData.set(i, newRow);
					}
					break;
				}
			}
		});
	}

	public void removeWorkerById(String uuid) {
		workersCache.removeIf(w -> w.getUuid().equals(uuid));
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if (tableModel.getValueAt(i, 0).equals(uuid)) {
					tableModel.removeRow(i);
					if (i < cachedData.size()) {
						cachedData.remove(i);
					}
					break;
				}
			}
		});
	}

	public void clearAllWorkers() {
		workersCache.clear();
		SwingUtilities.invokeLater(() -> {
			tableModel.setRowCount(0);
			cachedData.clear();
		});
	}

	private Worker parseWorkerFromJsonToWorker(JsonNode worker) {
		try {
			Worker w = new Worker();
			if (worker.has("uuid"))
				w.setUuid(worker.get("uuid").asText());
			if (worker.has("name"))
				w.setName(worker.get("name").asText());
			if (worker.has("salary"))
				w.setSalary(worker.get("salary").floatValue());

			if (worker.has("coordinates") && !worker.get("coordinates").isNull()) {
				JsonNode coords = worker.get("coordinates");
				Coordinates c = new Coordinates();
				if (coords.has("x"))
					c.setX(coords.get("x").asLong());
				if (coords.has("y"))
					c.setY((float) coords.get("y").asDouble());
				w.setCoordinates(c);
			}

			if (worker.has("status") && !worker.get("status").isNull()) {
				w.setStatus(Status.valueOf(worker.get("status").asText()));
			}

			if (worker.has("startDate") && !worker.get("startDate").isNull()) {
				JsonNode dateNode = worker.get("startDate");
				if (dateNode.isArray() && dateNode.size() >= 3) {
					w.setStartDate(
							LocalDate.of(dateNode.get(0).asInt(), dateNode.get(1).asInt(), dateNode.get(2).asInt()));
				}
			}

			if (worker.has("endDate") && !worker.get("endDate").isNull()) {
				JsonNode dateNode = worker.get("endDate");
				if (dateNode.isArray() && dateNode.size() >= 3) {
					w.setEndDate(
							LocalDate.of(dateNode.get(0).asInt(), dateNode.get(1).asInt(), dateNode.get(2).asInt()));
				}
			}

			if (worker.has("organization") && !worker.get("organization").isNull()) {
				JsonNode orgNode = worker.get("organization");
				Organization org = new Organization();
				if (orgNode.has("annualTurnover")) {
					org.setAnnualTurnover(orgNode.get("annualTurnover").asInt());
				}
				if (orgNode.has("type") && !orgNode.get("type").isNull()) {
					org.setType(OrganizationType.valueOf(orgNode.get("type").asText()));
				}
				if (orgNode.has("officialAddress") && !orgNode.get("officialAddress").isNull()) {
					JsonNode addrNode = orgNode.get("officialAddress");
					Address addr = new Address();
					if (addrNode.has("street")) {
						addr.setStreet(addrNode.get("street").asText());
					}
					org.setOfficialAddress(addr);
				}
				w.setOrganization(org);
			}

			return w;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Worker> getWorkersList() {
		return new ArrayList<>(workersCache);
	}
}
