package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class ReadFilePanel extends JPanel {
	private final LocaleManager lm;
	private final String currentUser;
	private final JTextField fileNameField;
	private final JTextArea outputArea;
	private final JButton readButton;
	private final JButton stopButton;
	private final JLabel loadingLabel;
	private final JProgressBar progressBar;
	private final JLabel titleLabel;
	private final JLabel promptLabel;

	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final AtomicBoolean shouldStop = new AtomicBoolean(false);
	private final AtomicBoolean isConfirmingStop = new AtomicBoolean(false);

	private String currentFileName;
	private boolean isWaitingForCommand = false;

	public ReadFilePanel(String username) {
		this.currentUser = username;
		this.lm = LocaleManager.getInstance();

		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setOpaque(false);
		mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));
		mainPanel.add(titleLabel);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setOpaque(false);
		inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.setMaximumSize(new Dimension(600, 300));
		inputPanel.setPreferredSize(new Dimension(600, 300));

		promptLabel = new JLabel();
		promptLabel.setForeground(Color.WHITE);
		promptLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(promptLabel);
		inputPanel.add(Box.createVerticalStrut(15));

		fileNameField = new JTextField();
		fileNameField.setFont(new Font("Arial", Font.PLAIN, 18));
		fileNameField.setBackground(new Color(48, 48, 48));
		fileNameField.setForeground(Color.WHITE);
		fileNameField.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
		fileNameField.setMaximumSize(new Dimension(500, 50));
		fileNameField.setPreferredSize(new Dimension(500, 50));
		fileNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(fileNameField);
		inputPanel.add(Box.createVerticalStrut(25));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		readButton = new JButton();
		readButton.setBackground(new Color(48, 48, 48));
		readButton.setForeground(Color.WHITE);
		readButton.setFont(new Font("Arial", Font.BOLD, 18));
		readButton.setFocusPainted(false);
		readButton.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
		readButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		readButton.addActionListener(e -> onReadFile());
		buttonPanel.add(readButton);

		stopButton = new JButton();
		stopButton.setBackground(new Color(48, 48, 48));
		stopButton.setForeground(Color.WHITE);
		stopButton.setFont(new Font("Arial", Font.BOLD, 18));
		stopButton.setFocusPainted(false);
		stopButton.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
		stopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		stopButton.setEnabled(false);
		stopButton.addActionListener(e -> confirmStopExecution());
		buttonPanel.add(stopButton);

		inputPanel.add(buttonPanel);
		inputPanel.add(Box.createVerticalStrut(20));

		loadingLabel = new JLabel(" ");
		loadingLabel.setForeground(new Color(100, 200, 100));
		loadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
		loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(loadingLabel);
		inputPanel.add(Box.createVerticalStrut(10));

		progressBar = new JProgressBar(0, 100);
		progressBar.setForeground(new Color(50, 150, 50));
		progressBar.setBackground(new Color(48, 48, 48));
		progressBar.setBorderPainted(false);
		progressBar.setPreferredSize(new Dimension(500, 10));
		progressBar.setMaximumSize(new Dimension(500, 10));
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
		progressBar.setVisible(false);
		inputPanel.add(progressBar);

		mainPanel.add(inputPanel);
		mainPanel.add(Box.createVerticalStrut(30));

		outputArea = new JTextArea();
		outputArea.setBackground(Color.BLACK);
		outputArea.setForeground(new Color(200, 200, 200));
		outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		outputArea.setEditable(false);
		outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
		scrollPane.getViewport().setBackground(Color.BLACK);
		scrollPane.setPreferredSize(new Dimension(700, 300));
		scrollPane.setMaximumSize(new Dimension(700, 300));
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

		mainPanel.add(scrollPane);
		mainPanel.add(Box.createVerticalStrut(30));

		add(mainPanel, gbc);
		lm.addLocaleChangeListener(this::updateTexts);

		updateTexts();
	}

	private void updateTexts() {
		titleLabel.setText(lm.getString("command.read_file"));
		promptLabel.setText(lm.getString("form.enter_filename"));
		readButton.setText(lm.getString("button.load_file"));
		stopButton.setText(lm.getString("button.stop"));
	}

	private void onReadFile() {
		currentFileName = fileNameField.getText().trim();
		if (currentFileName.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_filename"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		outputArea.setText("");
		outputArea.append(lm.getString("message.executing_script") + ": " + currentFileName + "\n\n");

		fileNameField.setEnabled(false);
		readButton.setEnabled(false);
		stopButton.setEnabled(true);
		progressBar.setVisible(true);
		progressBar.setValue(0);

		isRunning.set(true);
		shouldStop.set(false);
		isConfirmingStop.set(false);
		isWaitingForCommand = false;

		Thread executionThread = new Thread(this::executeScript);
		executionThread.start();
	}

	private void confirmStopExecution() {
		if (isRunning.get() && !isConfirmingStop.get()) {
			isConfirmingStop.set(true);
			int confirm = JOptionPane.showConfirmDialog(this, lm.getString("confirm.stop_script"),
					lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			isConfirmingStop.set(false);

			if (confirm == JOptionPane.YES_OPTION) {
				shouldStop.set(true);
				stopButton.setEnabled(false);
				SwingUtilities.invokeLater(() -> {
					if (isWaitingForCommand) {
						loadingLabel.setText(lm.getString("message.stopping") + " (завершаем текущую команду...)");
					} else {
						loadingLabel.setText(lm.getString("message.stopping"));
					}
				});
			}
		}
	}

	public boolean isScriptRunning() {
		return isRunning.get();
	}

	public void stopWithoutConfirm() {
		if (isRunning.get()) {
			shouldStop.set(true);
			stopButton.setEnabled(false);
			SwingUtilities.invokeLater(() -> loadingLabel.setText(lm.getString("message.stopping")));
		}
	}

	private void executeScript() {
		try (BufferedReader reader = new BufferedReader(new FileReader(currentFileName))) {
			String line;
			ArrayList<String> allCommands = new ArrayList<>();

			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					allCommands.add(line);
				}
			}

			int totalCommands = allCommands.size();

			SwingUtilities.invokeLater(
					() -> outputArea.append(lm.getString("message.commands_found") + ": " + totalCommands + "\n\n"));

			int successCount = 0;
			int errorCount = 0;

			for (int i = 0; i < allCommands.size(); i++) {
				if (shouldStop.get()) {
					int finalI = i;
					SwingUtilities.invokeLater(() -> {
						outputArea.append("\n" + lm.getString("message.execution_interrupted") + "\n");
						outputArea.append(lm.getString("message.commands_executed") + ": " + finalI + " / "
								+ totalCommands + "\n");
						loadingLabel.setText(lm.getString("message.done"));
						progressBar.setValue(0);
					});
					break;
				}

				int progress = ((i + 1) * 100) / totalCommands;
				final int currentLine = i + 1;
				final String command = allCommands.get(i);

				SwingUtilities.invokeLater(() -> {
					progressBar.setValue(progress);
					loadingLabel.setText(lm.getString("message.executing") + ": " + progress + "% (" + currentLine + "/"
							+ totalCommands + ")");
					outputArea.append("[" + currentLine + "/" + totalCommands + "] " + command + "\n");
				});

				isWaitingForCommand = true;
				boolean success = sendCommandToServer(command);
				isWaitingForCommand = false;

				if (success) {
					successCount++;
				} else {
					errorCount++;
				}

				if (shouldStop.get()) {
					int finalI = i + 1;
					SwingUtilities.invokeLater(() -> {
						outputArea.append("\n" + lm.getString("message.execution_interrupted") + "\n");
						outputArea.append(lm.getString("message.commands_executed") + ": " + finalI + " / "
								+ totalCommands + "\n");
						loadingLabel.setText(lm.getString("message.done"));
						progressBar.setValue(0);
					});
					break;
				}

				try {
					sleep(50);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					break;
				}
			}

			if (!shouldStop.get()) {
				final int finalSuccess = successCount;
				final int finalError = errorCount;
				SwingUtilities.invokeLater(() -> {
					outputArea.append("\n" + lm.getString("message.script_completed") + "\n");
					outputArea.append(lm.getString("message.total_executed") + ": " + totalCommands + "\n");
					outputArea.append(lm.getString("message.success") + ": " + finalSuccess
							+ lm.getString("message.errors") + ": " + finalError + "\n");
					loadingLabel.setText(lm.getString("message.done"));
					progressBar.setValue(100);
				});
			}

		} catch (IOException e) {
			SwingUtilities.invokeLater(() -> outputArea
					.append("\n" + lm.getString("error.file_not_found") + ": '" + currentFileName + "'\n"));
		} finally {
			// ignore
			SwingUtilities.invokeLater(() -> {
				fileNameField.setEnabled(true);
				readButton.setEnabled(true);
				stopButton.setEnabled(false);
				progressBar.setVisible(false);
				loadingLabel.setText(" ");
				isRunning.set(false);
				isWaitingForCommand = false;
			});
		}
	}

	private boolean sendCommandToServer(String commandLine) {
		try {
			String[] parts = commandLine.trim().split("\\s+", 2);
			String commandName = parts[0];
			String argument = parts.length > 1 ? parts[1] : null;

			ClientCommand.ClientCommandBuilder builder = ClientCommand.builder().nameCommand(commandName)
					.user(currentUser).language(lm.getCurrentLang());
			if ((commandName.equals("add") || commandName.equals("update")) && argument != null
					&& argument.contains("{")) {
				WorkerData workerData = parseWorkerDataFromBraces(argument);
				if (workerData != null) {
					builder.data(workerData);
					if (commandName.equals("update")) {
						String id = extractUpdateId(commandLine);
						if (id != null) {
							builder.argumentCommand(id);
						}
					}
				} else {
					SwingUtilities
							.invokeLater(() -> outputArea.append(lm.getString("error.invalid_data_format") + "\n"));
					return false;
				}
			} else if (argument != null) {
				builder.argumentCommand(argument);
			}

			ClientCommand command = builder.build();
			ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

			SwingUtilities.invokeLater(() -> {
				if (response != null && response.execution()) {
					String msg = response.message() != null ? response.message() : lm.getString("message.success");
					outputArea.append(msg + "\n");
				} else {
					String errorMsg = (response != null && response.message() != null)
							? response.message()
							: lm.getString("error.unknown");
					outputArea.append(errorMsg + "\n");
				}
			});

			return response != null && response.execution();

		} catch (Exception e) {
			SwingUtilities.invokeLater(() -> outputArea.append(lm.getString("error.prefix") + e.getMessage() + "\n"));
			return false;
		}
	}

	private String extractUpdateId(String commandLine) {
		String[] parts = commandLine.trim().split("\\s+");
		if (parts.length >= 2) {
			String potentialId = parts[1];
			if (!potentialId.contains("{")) {
				return potentialId;
			}
		}
		return null;
	}

	private WorkerData parseWorkerDataFromBraces(String argument) {
		try {
			int startBrace = argument.indexOf('{');
			int endBrace = argument.lastIndexOf('}');

			if (startBrace == -1 || endBrace == -1) {
				outputArea.append(lm.getString("error.missing_braces") + "\n");
				return null;
			}

			String bracesContent = argument.substring(startBrace + 1, endBrace);
			String[] parts = bracesContent.split(";");

			for (int i = 0; i < parts.length; i++) {
				parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
			}

			if (parts.length < 10) {
				outputArea.append(
						lm.getString("error.insufficient_data") + " (need 10 fields, got " + parts.length + ")\n");
				return null;
			}

			String name = parts[0];
			String x = parts[1];
			String y = parts[2];
			String salary = parts[3];
			String startDate = parts[4];
			String endDate = parts[5].isEmpty() || "null".equals(parts[5]) ? null : parts[5];
			String status = parts[6];
			String annualTurnover = parts[7];
			String type = parts[8];
			String street = parts[9];

			if (name.isEmpty()) {
				outputArea.append(lm.getString("error.empty_name") + "\n");
				return null;
			}

			double xVal = Double.parseDouble(x);
			if (xVal > 10) {
				outputArea.append(lm.getString("error.x_max") + "\n");
				return null;
			}

			double yVal = Double.parseDouble(y);
			if (yVal <= -644) {
				outputArea.append(lm.getString("error.y_min") + "\n");
				return null;
			}

			double salaryVal = Double.parseDouble(salary);
			if (salaryVal <= 0) {
				outputArea.append(lm.getString("error.salary_positive") + "\n");
				return null;
			}

			int turnoverVal = Integer.parseInt(annualTurnover);
			if (turnoverVal <= 0) {
				outputArea.append(lm.getString("error.turnover_positive") + "\n");
				return null;
			}

			if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
				outputArea.append(lm.getString("error.invalid_date_format") + " (startDate)\n");
				return null;
			}

			if (endDate != null && !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
				outputArea.append(lm.getString("error.invalid_date_format") + " (endDate)\n");
				return null;
			}

			String statusId = switch (status) {
				case "HIRED" -> "2";
				case "RECOMMENDED_FOR_PROMOTION" -> "3";
				case "PROBATION" -> "4";
				default -> "1";
			};

			String typeId = switch (type) {
				case "PUBLIC" -> "2";
				case "GOVERNMENT" -> "3";
				case "TRUST" -> "4";
				case "OPEN_JOINT_STOCK_COMPANY" -> "5";
				default -> "1";
			};

			CoordinatesData coordinates = new CoordinatesData(x, y);
			AddressData address = new AddressData(street);
			TypeOrganizationDate orgType = new TypeOrganizationDate(typeId);
			OrganizationData organization = new OrganizationData(annualTurnover, orgType, address);
			DataStatus dataStatus = new DataStatus(statusId);

			return new WorkerData(name, coordinates, salary, startDate, endDate, dataStatus, organization);

		} catch (NumberFormatException e) {
			outputArea.append(lm.getString("error.invalid_number") + ": " + e.getMessage() + "\n");
			return null;
		} catch (Exception e) {
			outputArea.append(lm.getString("error.parse") + ": " + e.getMessage() + "\n");
			return null;
		}
	}
}
