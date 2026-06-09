package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SearchByOrganizationPanel extends JPanel {
	private final MainAppPanel parent;
	private final String currentUser;
	private final LocaleManager lm;
	private final JTextField turnoverField;
	private final JComboBox<String> typeComboBox;
	private final JTextField addressField;
	private final JLabel titleLabel;
	private final JLabel turnoverLabel;
	private final JLabel typeLabel;
	private final JLabel addressLabel;
	private final JButton searchButton;

	private final Map<String, String> typeToId = new HashMap<>();
	private final Map<String, String> typeToKey = new HashMap<>();

	public SearchByOrganizationPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		this.lm = LocaleManager.getInstance();

		typeToId.put("COMMERCIAL", "1");
		typeToId.put("PUBLIC", "2");
		typeToId.put("GOVERNMENT", "3");
		typeToId.put("TRUST", "4");
		typeToId.put("OPEN_JOINT_STOCK_COMPANY", "5");
		typeToKey.put("COMMERCIAL", "organization.type.commercial");
		typeToKey.put("PUBLIC", "organization.type.public");
		typeToKey.put("GOVERNMENT", "organization.type.government");
		typeToKey.put("TRUST", "organization.type.trust");
		typeToKey.put("OPEN_JOINT_STOCK_COMPANY", "organization.type.open_joint_stock");

		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		titleLabel = new JLabel();
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

		turnoverLabel = new JLabel();
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

		typeLabel = new JLabel();
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
				if (value != null) {
					String typeKey = typeToKey.get(value.toString());
					if (typeKey != null) {
						setText(lm.getString(typeKey));
					}
				}
				return this;
			}
		});
		formPanel.add(typeComboBox);
		formPanel.add(Box.createVerticalStrut(15));
		addressLabel = new JLabel();
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

		searchButton = getJButton();
		formPanel.add(searchButton);

		gbc.gridy = 1;
		add(formPanel, gbc);
		lm.addLocaleChangeListener(this::updateTexts);
		updateTexts();
	}

	private void updateTexts() {
		titleLabel.setText(lm.getString("command.search_organization"));
		turnoverLabel.setText(lm.getString("form.enter_turnover"));
		typeLabel.setText(lm.getString("form.select_organization_type"));
		addressLabel.setText(lm.getString("form.enter_address"));
		searchButton.setText(lm.getString("command.search_organization"));
		typeComboBox.repaint();
	}

	@Nonnull
	private JButton getJButton() {
		JButton button = new JButton();
		button.setBackground(new Color(48, 48, 48));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 22));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setMaximumSize(new Dimension(500, 55));
		button.setPreferredSize(new Dimension(500, 55));
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.addActionListener(e -> onSearch());
		return button;
	}

	private void onSearch() {
		String turnoverStr = turnoverField.getText().trim();
		String typeName = (String) typeComboBox.getSelectedItem();
		String address = addressField.getText().trim();

		if (turnoverStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_turnover"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (address.isEmpty()) {
			JOptionPane.showMessageDialog(this, lm.getString("error.empty_address"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Long.parseLong(turnoverStr);

			String typeId = typeToId.get(typeName);
			if (typeId == null) {
				typeId = "1";
			}

			String searchData = turnoverStr + ":" + typeId + ":" + address;

			setEnabled(false);

			new Thread(() -> {
				try {
					ClientCommand command = ClientCommand.builder().nameCommand("count_by_organization")
							.user(currentUser).argumentCommand(searchData).build();

					ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

					SwingUtilities.invokeLater(() -> {
						setEnabled(true);

						if (response != null && response.execution()) {
							parent.showSearchResult(currentUser, searchData);
						} else {
							String errorMsg = response != null ? response.message() : lm.getString("error.prefix");
							JOptionPane.showMessageDialog(SearchByOrganizationPanel.this,
									lm.getString("error.prefix") + errorMsg, lm.getString("message.error"),
									JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						setEnabled(true);
						JOptionPane.showMessageDialog(SearchByOrganizationPanel.this,
								lm.getString("error.connection") + ": " + ex.getMessage(),
								lm.getString("message.error"), JOptionPane.ERROR_MESSAGE);
					});
				}
			}).start();

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, lm.getString("error.invalid_number"), lm.getString("message.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
