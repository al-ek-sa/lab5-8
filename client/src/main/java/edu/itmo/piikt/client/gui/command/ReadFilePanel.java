package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
public class ReadFilePanel extends JPanel {
	private MainAppPanel parent;
	private String currentUser;
	private JTextField fileNameField;

	public ReadFilePanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		JLabel titleLabel = new JLabel("ЧТЕНИЕ ИЗ ФАЙЛА");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
		gbc.insets = new Insets(0, 0, 50, 0);
		add(titleLabel, gbc);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.setMaximumSize(new Dimension(500, 200));
		formPanel.setPreferredSize(new Dimension(500, 200));

		JLabel promptLabel = new JLabel("Введите название файла");
		promptLabel.setForeground(Color.WHITE);
		promptLabel.setFont(new Font("Arial", Font.PLAIN, 24));
		promptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(promptLabel);
		formPanel.add(Box.createVerticalStrut(10));

		fileNameField = new JTextField();
		fileNameField.setFont(new Font("Arial", Font.PLAIN, 18));
		fileNameField.setBackground(new Color(48, 48, 48));
		fileNameField.setForeground(Color.WHITE);
		fileNameField.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
		fileNameField.setMaximumSize(new Dimension(500, 50));
		fileNameField.setPreferredSize(new Dimension(500, 50));
		fileNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(fileNameField);
		formPanel.add(Box.createVerticalStrut(25));

		JButton readButton = new JButton("ЧИТАТЬ ФАЙЛ");
		readButton.setBackground(new Color(48, 48, 48));
		readButton.setForeground(Color.WHITE);
		readButton.setFont(new Font("Arial", Font.BOLD, 25));
		readButton.setFocusPainted(false);
		readButton.setBorder(BorderFactory.createEmptyBorder(12, 50, 12, 50));
		readButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		readButton.setMaximumSize(new Dimension(500, 60));
		readButton.setPreferredSize(new Dimension(500, 60));
		readButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		readButton.addActionListener(e -> onReadFile());
		formPanel.add(readButton);

		gbc.gridy = 1;
		add(formPanel, gbc);
	}

	private void onReadFile() {
		String fileName = fileNameField.getText().trim();
		if (fileName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите название файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Чтение файла: " + fileName, "Информация", JOptionPane.INFORMATION_MESSAGE);
	}
}
