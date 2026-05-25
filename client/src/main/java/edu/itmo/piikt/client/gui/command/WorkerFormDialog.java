package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
public class WorkerFormDialog extends JDialog {
	private MainAppPanel parent;
	private String currentUser;
	private JTextField nameField;
	private JTextField coordinateXField;
	private JTextField coordinateYField;
	private JTextField salaryField;
	private JTextField startDateField;
	private JTextField endDateField;
	private JComboBox<String> statusComboBox;
	private JTextField annualTurnoverField;
	private JComboBox<String> organizationTypeComboBox;
	private JTextField addressField;
	private boolean isEditMode;
	private int editRowIndex;
	private Object[] editRowData;
	private boolean confirmed = false;

	public WorkerFormDialog(JFrame parent, MainAppPanel mainAppPanel, String username) {
		this(parent, mainAppPanel, username, false, -1, null);
	}

	public WorkerFormDialog(JFrame parent, MainAppPanel mainAppPanel, String username, boolean isEditMode, int rowIndex,
			Object[] rowData) {
		super(parent, isEditMode ? "Изменить работника" : "Добавить работника", true);
		this.parent = mainAppPanel;
		this.currentUser = username;
		this.isEditMode = isEditMode;
		this.editRowIndex = rowIndex;
		this.editRowData = rowData;

		setSize(450, 650);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(48, 48, 48));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(isEditMode ? "ИЗМЕНЕНИЕ РАБОТНИКА" : "ДОБАВЛЕНИЕ РАБОТНИКА");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		mainPanel.add(titleLabel);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 15, 5, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridwidth = 2;

		gbc.gridy = 0;
		JLabel nameLabel = new JLabel("Имя:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(nameLabel, gbc);

		gbc.gridy = 1;
		nameField = new JTextField();
		nameField.setBackground(new Color(60, 60, 70));
		nameField.setForeground(Color.WHITE);
		nameField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		nameField.setCaretColor(Color.WHITE);
		formPanel.add(nameField, gbc);

		gbc.gridy = 2;
		JLabel coordinateXLabel = new JLabel("Координата X:");
		coordinateXLabel.setForeground(Color.WHITE);
		coordinateXLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(coordinateXLabel, gbc);

		gbc.gridy = 3;
		coordinateXField = new JTextField();
		coordinateXField.setBackground(new Color(60, 60, 70));
		coordinateXField.setForeground(Color.WHITE);
		coordinateXField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(coordinateXField, gbc);

		gbc.gridy = 4;
		JLabel coordinateYLabel = new JLabel("Координата Y:");
		coordinateYLabel.setForeground(Color.WHITE);
		coordinateYLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(coordinateYLabel, gbc);

		gbc.gridy = 5;
		coordinateYField = new JTextField();
		coordinateYField.setBackground(new Color(60, 60, 70));
		coordinateYField.setForeground(Color.WHITE);
		coordinateYField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(coordinateYField, gbc);

		gbc.gridy = 6;
		JLabel salaryLabel = new JLabel("Зарплата:");
		salaryLabel.setForeground(Color.WHITE);
		salaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(salaryLabel, gbc);

		gbc.gridy = 7;
		salaryField = new JTextField();
		salaryField.setBackground(new Color(60, 60, 70));
		salaryField.setForeground(Color.WHITE);
		salaryField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(salaryField, gbc);

		gbc.gridy = 8;
		JLabel startDateLabel = new JLabel("Дата начала работы (ГГГГ-ММ-ДД):");
		startDateLabel.setForeground(Color.WHITE);
		startDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(startDateLabel, gbc);

		gbc.gridy = 9;
		startDateField = new JTextField();
		startDateField.setBackground(new Color(60, 60, 70));
		startDateField.setForeground(Color.WHITE);
		startDateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(startDateField, gbc);

		gbc.gridy = 10;
		JLabel endDateLabel = new JLabel("Дата окончания работы:");
		endDateLabel.setForeground(Color.WHITE);
		endDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(endDateLabel, gbc);

		gbc.gridy = 11;
		endDateField = new JTextField();
		endDateField.setBackground(new Color(60, 60, 70));
		endDateField.setForeground(Color.WHITE);
		endDateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(endDateField, gbc);

		gbc.gridy = 12;
		JLabel statusLabel = new JLabel("Статус:");
		statusLabel.setForeground(Color.WHITE);
		statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(statusLabel, gbc);

		gbc.gridy = 13;
		String[] statuses = {"HIRED", "FIRED", "RECOMMENDED_FOR_PROMOTION", "PROBATION"};
		statusComboBox = new JComboBox<>(statuses);
		statusComboBox.setBackground(new Color(60, 60, 70));
		statusComboBox.setForeground(Color.WHITE);
		statusComboBox.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(statusComboBox, gbc);

		gbc.gridy = 14;
		JLabel turnoverLabel = new JLabel("Годовой оборот организации:");
		turnoverLabel.setForeground(Color.WHITE);
		turnoverLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(turnoverLabel, gbc);

		gbc.gridy = 15;
		annualTurnoverField = new JTextField();
		annualTurnoverField.setBackground(new Color(60, 60, 70));
		annualTurnoverField.setForeground(Color.WHITE);
		annualTurnoverField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(annualTurnoverField, gbc);

		gbc.gridy = 16;
		JLabel orgTypeLabel = new JLabel("Тип организации:");
		orgTypeLabel.setForeground(Color.WHITE);
		orgTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(orgTypeLabel, gbc);

		gbc.gridy = 17;
		String[] orgTypes = {"COMMERCIAL", "PUBLIC", "GOVERNMENT", "TRUST", "OPEN_JOINT_STOCK_COMPANY"};
		organizationTypeComboBox = new JComboBox<>(orgTypes);
		organizationTypeComboBox.setBackground(new Color(60, 60, 70));
		organizationTypeComboBox.setForeground(Color.WHITE);
		organizationTypeComboBox.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(organizationTypeComboBox, gbc);

		gbc.gridy = 18;
		JLabel addressLabel = new JLabel("Адрес организации:");
		addressLabel.setForeground(Color.WHITE);
		addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		formPanel.add(addressLabel, gbc);

		gbc.gridy = 19;
		addressField = new JTextField();
		addressField.setBackground(new Color(60, 60, 70));
		addressField.setForeground(Color.WHITE);
		addressField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		formPanel.add(addressField, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

		JButton saveButton = new JButton(isEditMode ? "ОБНОВИТЬ" : "СОХРАНИТЬ");
		saveButton.setBackground(new Color(0, 0, 0));
		saveButton.setForeground(Color.WHITE);
		saveButton.setFont(new Font("Arial", Font.BOLD, 14));
		saveButton.setFocusPainted(false);
		saveButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
		saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		saveButton.addActionListener(e -> onSave());

		JButton cancelButton = new JButton("ОТМЕНА");
		cancelButton.setBackground(new Color(0, 0, 0));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
		cancelButton.setFocusPainted(false);
		cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
		cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cancelButton.addActionListener(e -> dispose());

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		JScrollPane scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(new Color(48, 48, 48));
		scrollPane.setPreferredSize(new Dimension(450, 450));

		mainPanel.add(scrollPane);
		mainPanel.add(buttonPanel);

		add(mainPanel);

		if (isEditMode && editRowData != null) {
			fillFields(editRowData);
		}
	}

	private void fillFields(Object[] rowData) {
		if (rowData.length > 1)
			nameField.setText(rowData[1] != null ? rowData[1].toString() : "");
		if (rowData.length > 2)
			coordinateXField.setText(rowData[2] != null ? rowData[2].toString() : "");
		if (rowData.length > 3)
			coordinateYField.setText(rowData[3] != null ? rowData[3].toString() : "");
		if (rowData.length > 4)
			salaryField.setText(rowData[4] != null ? rowData[4].toString() : "");
		if (rowData.length > 5)
			startDateField.setText(rowData[5] != null ? rowData[5].toString() : "");
		if (rowData.length > 6)
			endDateField.setText(rowData[6] != null ? rowData[6].toString() : "");
		if (rowData.length > 7 && rowData[7] != null)
			statusComboBox.setSelectedItem(rowData[7].toString());
		if (rowData.length > 8)
			annualTurnoverField.setText(rowData[8] != null ? rowData[8].toString() : "");
		if (rowData.length > 9 && rowData[9] != null)
			organizationTypeComboBox.setSelectedItem(rowData[9].toString());
		if (rowData.length > 10)
			addressField.setText(rowData[10] != null ? rowData[10].toString() : "");
	}

	private void onSave() {
		if (nameField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите имя", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Object[] newWorkerData = new Object[]{null, nameField.getText().trim(), coordinateXField.getText().trim(),
				coordinateYField.getText().trim(), salaryField.getText().trim(), startDateField.getText().trim(),
				endDateField.getText().trim(), statusComboBox.getSelectedItem(), annualTurnoverField.getText().trim(),
				organizationTypeComboBox.getSelectedItem(), addressField.getText().trim()};

		if (isEditMode) {
			parent.updateWorkerInTable(editRowIndex, newWorkerData);
		} else {
			parent.addWorkerToTable(newWorkerData);
		}

		confirmed = true;
		dispose();
	}
}
