package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class ShowPanel extends JPanel {
	private MainAppPanel parent;
	private String currentUser;
	private JTable resultTable;
	private DefaultTableModel tableModel;
	private JComboBox<String> sortColumnComboBox;

	public ShowPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("ВСЕ РАБОТНИКИ");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(titleLabel, BorderLayout.NORTH);

		String[] columnNames = {"id", "Имя", "Координата X", "Координата Y", "Зарплата", "Дата начала работы",
				"Дата окончания работы", "Статус", "Годовой оборот", "Тип организации", "Адрес организации"};

		JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		sortPanel.setOpaque(false);
		JLabel sortLabel = new JLabel("Сортировать по столбцу:");
		sortLabel.setForeground(Color.WHITE);
		sortLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		sortPanel.add(sortLabel);

		sortColumnComboBox = new JComboBox<>(columnNames);
		sortColumnComboBox.setBackground(new Color(48, 48, 48));
		sortColumnComboBox.setForeground(Color.WHITE);
		sortColumnComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		sortColumnComboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		sortPanel.add(sortColumnComboBox);

		JButton applySortButton = new JButton("ПРИМЕНИТЬ");
		applySortButton.setBackground(new Color(48, 48, 48));
		applySortButton.setForeground(Color.WHITE);
		applySortButton.setFont(new Font("Arial", Font.BOLD, 14));
		applySortButton.setFocusPainted(false);
		applySortButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		applySortButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		applySortButton.addActionListener(e -> applySort());
		sortPanel.add(applySortButton);

		add(sortPanel, BorderLayout.NORTH);

		tableModel = new DefaultTableModel(columnNames, 0) {
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

		JButton addButton = new JButton("ДОБАВИТЬ");
		styleButton(addButton);
		addButton.addActionListener(e -> onAdd());
		buttonPanel.add(addButton, gbc);

		JButton editButton = new JButton("ИЗМЕНИТЬ");
		styleButton(editButton);
		editButton.addActionListener(e -> onEdit());
		buttonPanel.add(editButton, gbc);

		JButton deleteButton = new JButton("УДАЛИТЬ");
		styleButton(deleteButton);
		deleteButton.addActionListener(e -> onDelete());
		buttonPanel.add(deleteButton, gbc);

		JButton deleteByDateButton = new JButton("УДАЛИТЬ ПО ДАТЕ");
		styleButton(deleteByDateButton);
		deleteByDateButton.addActionListener(e -> onDeleteByDate());
		buttonPanel.add(deleteByDateButton, gbc);

		JButton clearButton = new JButton("ОЧИСТИТЬ");
		styleButton(clearButton);
		clearButton.addActionListener(e -> onClear());
		buttonPanel.add(clearButton, gbc);

		add(buttonPanel, BorderLayout.SOUTH);

		addTestData();
	}

	private void styleButton(JButton button) {
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(180, 55));
		button.setMinimumSize(new Dimension(160, 50));
		button.setMaximumSize(new Dimension(180, 55));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalTextPosition(SwingConstants.CENTER);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setLayout(new GridBagLayout());
	}

	private void addTestData() {
	}

	private void onAdd() {
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		WorkerFormDialog dialog = new WorkerFormDialog(frame, parent, currentUser);
		dialog.setVisible(true);
	}

	private void onEdit() {
		int selectedRow = resultTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Выберите строку для изменения", "Ошибка", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Object[] rowData = new Object[tableModel.getColumnCount()];
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			rowData[i] = tableModel.getValueAt(selectedRow, i);
		}

		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		WorkerFormDialog dialog = new WorkerFormDialog(frame, parent, currentUser, true, selectedRow, rowData);
		dialog.setVisible(true);
	}

	private void onDelete() {
		int selectedRow = resultTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Выберите строку для удаления", "Ошибка", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, "Удалить выбранную строку?", "Подтверждение",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			tableModel.removeRow(selectedRow);
		}
	}

	private void applySort() {
		int columnIndex = sortColumnComboBox.getSelectedIndex();
		if (columnIndex == -1)
			return;

		int rowCount = tableModel.getRowCount();
		if (rowCount <= 1)
			return;

		Object[][] data = new Object[rowCount][tableModel.getColumnCount()];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < tableModel.getColumnCount(); j++) {
				data[i][j] = tableModel.getValueAt(i, j);
			}
		}

		final int col = columnIndex;
		Arrays.sort(data, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				Object val1 = o1[col];
				Object val2 = o2[col];
				if (val1 == null && val2 == null)
					return 0;
				if (val1 == null)
					return 1;
				if (val2 == null)
					return -1;
				if (val1 instanceof Number && val2 instanceof Number) {
					return Double.compare(((Number) val1).doubleValue(), ((Number) val2).doubleValue());
				}
				return val1.toString().compareTo(val2.toString());
			}
		});

		tableModel.setRowCount(0);
		for (Object[] row : data) {
			tableModel.addRow(row);
		}
	}

	private void onDeleteByDate() {
		String dateStr = JOptionPane.showInputDialog(this,
				"Введите дату (формат: ГГГГ-ММ-ДД):\nБудут удалены все работники, у которых\nдата принятия на работу ПОЗЖЕ введённой даты",
				"Удалить по дате", JOptionPane.QUESTION_MESSAGE);

		if (dateStr == null || dateStr.trim().isEmpty())
			return;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		try {
			Date targetDate = dateFormat.parse(dateStr.trim());
			for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
				String startDateStr = (String) tableModel.getValueAt(i, 5);
				if (startDateStr != null && !startDateStr.isEmpty()) {
					try {
						Date startDate = dateFormat.parse(startDateStr);
						if (startDate.after(targetDate)) {
							tableModel.removeRow(i);
						}
					} catch (ParseException e) {
					}
				}
			}
			JOptionPane.showMessageDialog(this, "Удаление завершено", "Результат", JOptionPane.INFORMATION_MESSAGE);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Неверный формат даты. Используйте формат: ГГГГ-ММ-ДД", "Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onClear() {
		int confirm = JOptionPane.showConfirmDialog(this, "Очистить всю таблицу?", "Подтверждение",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			tableModel.setRowCount(0);
		}
	}

	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	public void updateRow(int rowIndex, Object[] rowData) {
		for (int i = 0; i < rowData.length && i < tableModel.getColumnCount(); i++) {
			tableModel.setValueAt(rowData[i], rowIndex, i);
		}
	}
}
