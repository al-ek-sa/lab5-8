package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.data.AddressData;
import edu.itmo.piikt.common.data.CoordinatesData;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.data.organization.type.TypeOrganizationDate;
import edu.itmo.piikt.common.data.status.DataStatus;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

public class WorkerFormDialog extends JDialog {
	private final MainAppPanel parent;
	private final String currentUser;
	private final boolean isEdit;
	private final String editId;

	private JTextField nameField;
	private JTextField xField;
	private JTextField yField;
	private JTextField salaryField;
	private JTextField startDateField;
	private JTextField endDateField;
	private JComboBox<String> statusComboBox;
	private JTextField annualTurnoverField;
	private JComboBox<String> typeComboBox;
	private JTextField streetField;

	private JLabel nameLabel;
	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel salaryLabel;
	private JLabel startDateLabel;
	private JLabel endDateLabel;
	private JLabel statusLabel;
	private JLabel turnoverLabel;
	private JLabel typeLabel;
	private JLabel addressLabel;

	private JButton saveButton;
	private JButton cancelButton;

	private final String[] statuses = {"FIRED", "HIRED", "RECOMMENDED_FOR_PROMOTION", "PROBATION"};
	private final String[] types = {"COMMERCIAL", "PUBLIC", "GOVERNMENT", "TRUST", "OPEN_JOINT_STOCK_COMPANY"};

	private final LocaleManager lm = LocaleManager.getInstance();

	public WorkerFormDialog(JFrame parent, MainAppPanel mainAppPanel, String username) {
		this(parent, mainAppPanel, username, false, null, null);
	}

	public WorkerFormDialog(JFrame parent, MainAppPanel mainAppPanel, String username, boolean isEdit, String editId,
			Object[] editData) {
		super(parent, true);
		this.parent = mainAppPanel;
		this.currentUser = username;
		this.isEdit = isEdit;
		this.editId = editId;

		setSize(550, 720);
		setLocationRelativeTo(parent);
		setBackground(Color.BLACK);

		initComponents();
		applyLocale();

		if (isEdit && editData != null) {
			fillEditData(editData);
		}

		lm.addLocaleChangeListener(this::applyLocale);
	}

	private void initComponents() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(Color.BLACK);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 10, 5, 10);

		nameLabel = new JLabel();
		xLabel = new JLabel();
		yLabel = new JLabel();
		salaryLabel = new JLabel();
		startDateLabel = new JLabel();
		endDateLabel = new JLabel();
		statusLabel = new JLabel();
		turnoverLabel = new JLabel();
		typeLabel = new JLabel();
		addressLabel = new JLabel();
		nameField = new JTextField(20);
		xField = new JTextField(20);
		yField = new JTextField(20);
		salaryField = new JTextField(20);
		startDateField = new JTextField(20);
		endDateField = new JTextField(20);
		statusComboBox = new JComboBox<>(statuses);
		annualTurnoverField = new JTextField(20);
		typeComboBox = new JComboBox<>(types);
		streetField = new JTextField(20);

		styleTextField(nameField);
		styleTextField(xField);
		styleTextField(yField);
		styleTextField(salaryField);
		styleTextField(startDateField);
		styleTextField(endDateField);
		styleTextField(annualTurnoverField);
		styleTextField(streetField);
		styleComboBox(statusComboBox);
		styleComboBox(typeComboBox);

		addField(mainPanel, gbc, nameLabel, nameField, 0);
		addField(mainPanel, gbc, xLabel, xField, 1);
		addField(mainPanel, gbc, yLabel, yField, 2);
		addField(mainPanel, gbc, salaryLabel, salaryField, 3);
		addField(mainPanel, gbc, startDateLabel, startDateField, 4);
		addField(mainPanel, gbc, endDateLabel, endDateField, 5);
		addComboBox(mainPanel, gbc, statusLabel, statusComboBox, 6);
		addField(mainPanel, gbc, turnoverLabel, annualTurnoverField, 7);
		addComboBox(mainPanel, gbc, typeLabel, typeComboBox, 8);
		addField(mainPanel, gbc, addressLabel, streetField, 9);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setOpaque(false);

		saveButton = createStyledButton();
		cancelButton = createStyledButton();

		saveButton.addActionListener(e -> onSave());
		cancelButton.addActionListener(e -> dispose());

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		gbc.gridy = 10;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(buttonPanel, gbc);

		add(mainPanel);
	}

	private void styleTextField(JTextField field) {
		field.setBackground(new Color(48, 48, 48));
		field.setForeground(Color.WHITE);
		field.setCaretColor(Color.WHITE);
		field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
		field.setPreferredSize(new Dimension(250, 35));
	}

	private void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setBackground(new Color(48, 48, 48));
		comboBox.setForeground(Color.WHITE);
		comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		comboBox.setPreferredSize(new Dimension(250, 35));
	}

	private JButton createStyledButton() {
		JButton button = new JButton();
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private void addField(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField field, int y) {
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(label, gbc);
		gbc.gridx = 1;
		panel.add(field, gbc);
		gbc.gridx = 0;
	}

	private void addComboBox(JPanel panel, GridBagConstraints gbc, JLabel label, JComboBox<String> comboBox, int y) {
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(label, gbc);

		gbc.gridx = 1;
		panel.add(comboBox, gbc);
		gbc.gridx = 0;
	}

	public void applyLocale() {
		setTitle(isEdit ? lm.getString("form.edit_worker") : lm.getString("form.add_worker"));
		nameLabel.setText(lm.getString("form.name") + ":");
		xLabel.setText(lm.getString("form.x") + " (max 10):");
		yLabel.setText(lm.getString("form.y") + " (> -644):");
		salaryLabel.setText(lm.getString("form.salary") + " (> 0):");
		startDateLabel.setText(lm.getString("form.start_date") + ":");
		endDateLabel.setText(lm.getString("form.end_date") + ":");
		statusLabel.setText(lm.getString("form.status") + ":");
		turnoverLabel.setText(lm.getString("form.annual_turnover") + " (> 0):");
		typeLabel.setText(lm.getString("form.organization_type") + ":");
		addressLabel.setText(lm.getString("form.address") + ":");

		saveButton.setText(isEdit ? lm.getString("button.save") : lm.getString("button.add"));
		cancelButton.setText(lm.getString("button.cancel"));
	}

	private void fillEditData(Object[] editData) {
		if (editData != null) {
			nameField.setText(editData[1] != null ? editData[1].toString() : "");
			xField.setText(editData[2] != null ? editData[2].toString() : "");
			yField.setText(editData[3] != null ? editData[3].toString() : "");
			salaryField.setText(editData[4] != null ? editData[4].toString() : "");
			startDateField.setText(editData[5] != null ? editData[5].toString() : "");
			endDateField.setText(
					editData[6] != null && !"null".equals(editData[6].toString()) ? editData[6].toString() : "");
			statusComboBox.setSelectedItem(editData[7] != null ? editData[7].toString() : "FIRED");
			annualTurnoverField.setText(editData[8] != null ? editData[8].toString() : "");
			typeComboBox.setSelectedItem(editData[9] != null ? editData[9].toString() : "COMMERCIAL");
			streetField.setText(editData[10] != null ? editData[10].toString() : "");
		}
	}

	private void onSave() {
		try {
			String name = nameField.getText().trim();
			String xStr = xField.getText().trim();
			String yStr = yField.getText().trim();
			String salaryStr = salaryField.getText().trim();
			String startDateStr = startDateField.getText().trim();
			String endDateStr = endDateField.getText().trim();
			String status = (String) statusComboBox.getSelectedItem();
			String annualTurnoverStr = annualTurnoverField.getText().trim();
			String type = (String) typeComboBox.getSelectedItem();
			String street = streetField.getText().trim();

			if (name.isEmpty()) {
				showError("error.empty_name");
				return;
			}
			if (xStr.isEmpty()) {
				showError("error.empty_x");
				return;
			}
			if (yStr.isEmpty()) {
				showError("error.empty_y");
				return;
			}
			if (salaryStr.isEmpty()) {
				showError("error.empty_salary");
				return;
			}
			if (startDateStr.isEmpty()) {
				showError("error.empty_start_date");
				return;
			}
			if (annualTurnoverStr.isEmpty()) {
				showError("error.empty_turnover");
				return;
			}
			if (street.isEmpty()) {
				showError("error.empty_address");
				return;
			}

			if (!startDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
				showError("error.invalid_date_format");
				return;
			}

			double x = Double.parseDouble(xStr);
			double y = Double.parseDouble(yStr);
			double salary = Double.parseDouble(salaryStr);
			int annualTurnover = Integer.parseInt(annualTurnoverStr);

			if (x > 10) {
				showError("error.x_max");
				return;
			}
			if (y <= -644) {
				showError("error.y_min");
				return;
			}
			if (salary <= 0) {
				showError("error.salary_positive");
				return;
			}
			if (annualTurnover <= 0) {
				showError("error.turnover_positive");
				return;
			}

			assert status != null;
			String statusId = switch (status) {
				case "HIRED" -> "2";
				case "RECOMMENDED_FOR_PROMOTION" -> "3";
				case "PROBATION" -> "4";
				default -> "1";
			};

			assert type != null;
			String typeId = switch (type) {
				case "PUBLIC" -> "2";
				case "GOVERNMENT" -> "3";
				case "TRUST" -> "4";
				case "OPEN_JOINT_STOCK_COMPANY" -> "5";
				default -> "1";
			};

			CoordinatesData coordinates = new CoordinatesData(xStr, yStr);
			AddressData address = new AddressData(street);
			TypeOrganizationDate orgType = new TypeOrganizationDate(typeId);
			OrganizationData organization = new OrganizationData(annualTurnoverStr, orgType, address);
			DataStatus dataStatus = new DataStatus(statusId);

			WorkerData workerData = new WorkerData(name, coordinates, salaryStr, startDateStr,
					endDateStr.isEmpty() ? null : endDateStr, dataStatus, organization);

			setEnabled(false);

			ClientCommand command = ClientCommand.builder().nameCommand(isEdit ? "update" : "add").user(currentUser)
					.data(workerData).argumentCommand(isEdit && editId != null ? editId : null).build();

			new Thread(() -> {
				try {
					ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						if (response != null && response.execution()) {
							String msgKey = isEdit ? "message.edit_success" : "message.add_success";
							JOptionPane.showMessageDialog(this, lm.getString(msgKey), lm.getString("message.success"),
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
						} else {
							String errorMsg = response != null ? response.message() : lm.getString("error.unknown");
							JOptionPane.showMessageDialog(this, lm.getString("error.prefix") + errorMsg,
									lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						showErrorWithText(ex.getMessage());
					});
				}
			}).start();

		} catch (NumberFormatException e) {
			showError("error.invalid_number");
		} catch (DateTimeParseException e) {
			showError("error.invalid_date_format");
		} catch (Exception e) {
			showErrorWithText(e.getMessage());
		}
	}

	private void showError(String key) {
		JOptionPane.showMessageDialog(this, lm.getString(key), lm.getString("message.error"),
				JOptionPane.ERROR_MESSAGE);
	}

	private void showErrorWithText(String text) {
		JOptionPane.showMessageDialog(this, lm.getString("error.prefix") + text, lm.getString("message.error"),
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void dispose() {
		lm.removeLocaleChangeListener(this::applyLocale);
		super.dispose();
	}
}
