package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
public class SearchByOrganizationPanel extends JPanel {
	private MainAppPanel parent;
	private String currentUser;
	private JTextField turnoverField;
	private JComboBox<String> typeComboBox;
	private JTextField addressField;

	public SearchByOrganizationPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		JLabel titleLabel = new JLabel("ПОИСК ПО ОРГАНИЗАЦИИ");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gbc.insets = new Insets(0, 0, 40, 0);
		add(titleLabel, gbc);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.setMaximumSize(new Dimension(500, 450));
		formPanel.setPreferredSize(new Dimension(500, 450));

		JLabel turnoverLabel = new JLabel("Введите годовой оборот организации");
		turnoverLabel.setForeground(Color.WHITE);
		turnoverLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		turnoverLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(turnoverLabel);
		formPanel.add(Box.createVerticalStrut(5));

		turnoverField = new JTextField();
		turnoverField.setFont(new Font("Arial", Font.PLAIN, 16));
		turnoverField.setBackground(new Color(48, 48, 48));
		turnoverField.setForeground(Color.WHITE);
		turnoverField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		turnoverField.setCaretColor(Color.WHITE);
		turnoverField.setMaximumSize(new Dimension(500, 45));
		turnoverField.setPreferredSize(new Dimension(500, 45));
		turnoverField.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(turnoverField);
		formPanel.add(Box.createVerticalStrut(15));
		JLabel typeLabel = new JLabel("Выберите тип организации");
		typeLabel.setForeground(Color.WHITE);
		typeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(typeLabel);
		formPanel.add(Box.createVerticalStrut(5));

		String[] types = {"COMMERCIAL", "PUBLIC", "GOVERNMENT", "TRUST", "OPEN_JOINT_STOCK_COMPANY"};
		typeComboBox = new JComboBox<>(types);
		typeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		typeComboBox.setBackground(new Color(48, 48, 48));
		typeComboBox.setForeground(Color.WHITE);
		typeComboBox.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		typeComboBox.setMaximumSize(new Dimension(500, 45));
		typeComboBox.setPreferredSize(new Dimension(500, 45));
		typeComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		typeComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setForeground(Color.WHITE);
				setBackground(new Color(48, 48, 48));
				return this;
			}
		});
		formPanel.add(typeComboBox);
		formPanel.add(Box.createVerticalStrut(15));
		JLabel addressLabel = new JLabel("Введите адрес организации");
		addressLabel.setForeground(Color.WHITE);
		addressLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(addressLabel);
		formPanel.add(Box.createVerticalStrut(5));

		addressField = new JTextField();
		addressField.setFont(new Font("Arial", Font.PLAIN, 16));
		addressField.setBackground(new Color(48, 48, 48));
		addressField.setForeground(Color.WHITE);
		addressField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		addressField.setCaretColor(Color.WHITE);
		addressField.setMaximumSize(new Dimension(500, 45));
		addressField.setPreferredSize(new Dimension(500, 45));
		addressField.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.add(addressField);
		formPanel.add(Box.createVerticalStrut(25));
		JButton searchButton = new JButton("ПОИСК ПО ОРГАНИЗАЦИИ");
		searchButton.setBackground(new Color(48, 48, 48));
		searchButton.setForeground(Color.WHITE);
		searchButton.setFont(new Font("Arial", Font.BOLD, 22));
		searchButton.setFocusPainted(false);
		searchButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
		searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		searchButton.setMaximumSize(new Dimension(500, 55));
		searchButton.setPreferredSize(new Dimension(500, 55));
		searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchButton.addActionListener(e -> onSearch());
		formPanel.add(searchButton);

		gbc.gridy = 1;
		add(formPanel, gbc);
	}

	private void onSearch() {
		String turnover = turnoverField.getText().trim();
		String type = (String) typeComboBox.getSelectedItem();
		String address = addressField.getText().trim();

		if (turnover.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите годовой оборот организации", "Ошибка",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (address.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Введите адрес организации", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;
		}
		parent.showSearchResult(currentUser);
	}
}
