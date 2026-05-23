package edu.itmo.piikt.client.gui.register.email;

import edu.itmo.piikt.client.gui.RightContentPanel;

import javax.swing.*;
import java.awt.*;

public class CodeConfirmationPanel extends JPanel {
    private RightContentPanel parent;
    private JTextField[] codeFields;
    private String email;

    public CodeConfirmationPanel(RightContentPanel parent, String email) {
        this.parent = parent;
        this.email = email;
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

        JLabel registerLabel = new JLabel("РЕГИСТРАЦИЯ");
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(registerLabel, gbc);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int fixedWidth = 500;
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
            codeFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                    String text = codeFields[index].getText();
                    if (text.length() == 1 && index < 5) {
                        codeFields[index + 1].requestFocus();
                    } else if (text.length() == 0 && index > 0) {
                        codeFields[index - 1].requestFocus();
                    }
                }
            });

            codePanel.add(codeFields[i]);
        }
        formPanel.add(codePanel);
        formPanel.add(Box.createVerticalStrut(40));
        JButton confirmButton = new JButton("ПОДТВЕРДИТЬ");
        confirmButton.setBackground(new Color(48, 48, 48));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 35));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setMaximumSize(new Dimension(fixedWidth, 80));
        confirmButton.setPreferredSize(new Dimension(fixedWidth, 80));
        confirmButton.addActionListener(e -> onConfirmCode());
        formPanel.add(confirmButton);
        formPanel.add(Box.createVerticalStrut(15));
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
        formPanel.add(resendButton);
        formPanel.add(Box.createVerticalStrut(15));
        JButton backButton = new JButton("ВЕРНУТЬСЯ НАЗАД");
        backButton.setBackground(new Color(48, 48, 48));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(fixedWidth, 80));
        backButton.setPreferredSize(new Dimension(fixedWidth, 80));
        backButton.addActionListener(e -> parent.showPanel("REGISTER"));
        formPanel.add(backButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(formPanel, gbc);
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    private void onConfirmCode() {
        StringBuilder code = new StringBuilder();
        for (JTextField field : codeFields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите полный код подтверждения");
                return;
            }
            code.append(field.getText().trim());
        }
        JOptionPane.showMessageDialog(this, "Код подтверждён!");
        parent.showCompleteRegistration(email);
    }

    private void onResendCode() {
        JOptionPane.showMessageDialog(this, "Новый код отправлен на " + email);
        for (JTextField field : codeFields) {
            field.setText("");
        }
        codeFields[0].requestFocus();
    }
}