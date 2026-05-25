package edu.itmo.piikt.client.gui.register.email;

import edu.itmo.piikt.client.gui.RightContentPanel;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class CodeConfirmationPanel extends JPanel {
	private RightContentPanel parent;
	private JTextField[] codeFields;
	private String email;
	private String expectedCode;

	public CodeConfirmationPanel(RightContentPanel parent, String email, String expectedCode) {
		this.parent = parent;
		this.email = email;
		this.expectedCode = expectedCode;

		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;

		JLabel titleLabel = new JLabel("WORKERFLOW");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
		gbc.gridy = 0;
		gbc.insets = new Insets(50, 0, 20, 0);
		add(titleLabel, gbc);

		JLabel confirmTitleLabel = new JLabel("ПОДТВЕРЖДЕНИЕ");
		confirmTitleLabel.setForeground(Color.WHITE);
		confirmTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 40, 0);
		add(confirmTitleLabel, gbc);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int fixedWidth = 500;

		JPanel messagePanel = getJPanel(email, fixedWidth);

		formPanel.add(messagePanel);
		formPanel.add(Box.createVerticalStrut(30));

		JPanel codePanel = new JPanel();
		codePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
		codePanel.setOpaque(false);
		codePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		codeFields = new JTextField[6];
		for (int i = 0; i < 6; i++) {
			final int index = i;
			codeFields[i] = new JTextField(1);
			codeFields[i].setFont(new Font("Arial", Font.BOLD, 30));
			codeFields[i].setBackground(new Color(48, 48, 48));
			codeFields[i].setForeground(Color.WHITE);
			codeFields[i].setHorizontalAlignment(JTextField.CENTER);
			codeFields[i].setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
			codeFields[i].setPreferredSize(new Dimension(60, 60));
			codeFields[i].setMaximumSize(new Dimension(60, 60));

			codeFields[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if (!Character.isDigit(c)) {
						e.consume();
						return;
					}
					if (!codeFields[index].getText().isEmpty()) {
						codeFields[index].setText("");
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					String text = codeFields[index].getText();
					if (text.length() == 1 && index < 5) {
						codeFields[index + 1].requestFocus();
					}
					if (text.isEmpty() && index > 0 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						codeFields[index - 1].requestFocus();
					}
				}
			});

			codePanel.add(codeFields[i]);
		}
		formPanel.add(codePanel);
		formPanel.add(Box.createVerticalStrut(40));

		JButton confirmButton = getJButton(fixedWidth);
		formPanel.add(confirmButton);
		formPanel.add(Box.createVerticalStrut(15));

		JButton resendButton = getButton(fixedWidth);
		formPanel.add(resendButton);
		formPanel.add(Box.createVerticalStrut(15));

		JButton backButton = getJButton(parent, fixedWidth);
		formPanel.add(backButton);

		gbc.gridy = 2;
		gbc.insets = new Insets(30, 0, 0, 0);
		add(formPanel, gbc);

		gbc.gridy = 3;
		gbc.weighty = 1.0;
		add(Box.createVerticalGlue(), gbc);
	}

	@Nonnull
	private static JButton getJButton(RightContentPanel parent, int fixedWidth) {
		JButton backButton = new JButton("ВЕРНУТЬСЯ НАЗАД");
		backButton.setBackground(new Color(48, 48, 48));
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("Arial", Font.BOLD, 30));
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		backButton.setMaximumSize(new Dimension(fixedWidth, 80));
		backButton.setPreferredSize(new Dimension(fixedWidth, 80));
		backButton.addActionListener(e -> parent.showPanel("REGISTER"));
		return backButton;
	}

	@Nonnull
	private JButton getButton(int fixedWidth) {
		JButton resendButton = new JButton("ЗАПРОСИТЬ КОД ПОВТОРНО");
		resendButton.setBackground(new Color(48, 48, 48));
		resendButton.setForeground(Color.WHITE);
		resendButton.setFont(new Font("Arial", Font.BOLD, 25));
		resendButton.setFocusPainted(false);
		resendButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		resendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		resendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		resendButton.setMaximumSize(new Dimension(fixedWidth, 70));
		resendButton.setPreferredSize(new Dimension(fixedWidth, 70));
		resendButton.addActionListener(e -> onResendCode());
		return resendButton;
	}

	@Nonnull
	private JButton getJButton(int fixedWidth) {
		JButton confirmButton = new JButton("ПОДТВЕРДИТЬ");
		confirmButton.setBackground(new Color(48, 48, 48));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFont(new Font("Arial", Font.BOLD, 30));
		confirmButton.setFocusPainted(false);
		confirmButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		confirmButton.setMaximumSize(new Dimension(fixedWidth, 80));
		confirmButton.setPreferredSize(new Dimension(fixedWidth, 80));
		confirmButton.addActionListener(e -> onConfirmCode());
		return confirmButton;
	}

	@Nonnull
	private static JPanel getJPanel(String email, int fixedWidth) {
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.setOpaque(false);
		messagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messagePanel.setMaximumSize(new Dimension(fixedWidth, 100));
		messagePanel.setPreferredSize(new Dimension(fixedWidth, 100));

		JLabel messageLabel = new JLabel("На вашу почту направлен код подтверждения");
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messagePanel.add(messageLabel);

		JLabel emailLabel = new JLabel(email);
		emailLabel.setForeground(new Color(150, 150, 150));
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		messagePanel.add(emailLabel);
		return messagePanel;
	}

	private void onConfirmCode() {
		StringBuilder code = new StringBuilder();
		for (JTextField field : codeFields) {
			if (field.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Введите полный код подтверждения", "Ошибка",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			code.append(field.getText().trim());
		}

		if (code.toString().equals(expectedCode)) {
			System.out.println("Codes match! Showing complete registration");
			parent.showCompleteRegistration(email);
		} else {
			JOptionPane.showMessageDialog(this, "Неверный код подтверждения", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onResendCode() {
		String newCode = String.format("%06d", new java.security.SecureRandom().nextInt(1000000));
		this.expectedCode = newCode;

		try {
			ClientCommand command = ClientCommand.builder().nameCommand("register_email").email(email).data(newCode)
					.build();

			ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

			if (response != null && response.execution()) {
				JOptionPane.showMessageDialog(this, "Новый код отправлен на " + email, "Успех",
						JOptionPane.INFORMATION_MESSAGE);
				for (JTextField field : codeFields) {
					field.setText("");
				}
				codeFields[0].requestFocus();
			} else {
				String errorMsg = response != null ? response.message() : "Неизвестная ошибка";
				JOptionPane.showMessageDialog(this, "Ошибка: " + errorMsg, "Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Ошибка соединения: " + ex.getMessage(), "Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
