package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.RightContentPanel;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {
    private RightContentPanel parent;
    private String currentUser;

    public HelpPanel(RightContentPanel parent, String username) {
        this.parent = parent;
        this.currentUser = username;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("СПРАВКА");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 0, 30, 0);
        add(titleLabel, gbc);

        JTextArea helpText = new JTextArea();
        helpText.setText(
                "ДОСТУПНЫЕ КОМАНДЫ:\n\n" +
                        "help - вывести справку\n" +
                        "info - информация о коллекции\n" +
                        "show - показать все элементы\n" +
                        "add - добавить элемент\n" +
                        "update - обновить элемент\n" +
                        "remove_by_id - удалить по id\n" +
                        "clear - очистить коллекцию\n" +
                        "head - первый элемент\n" +
                        "history - история команд\n" +
                        "count_by_organization - количество по организации\n" +
                        "filter_contains_name - фильтр по имени\n" +
                        "print_field_desc_end_date - вывести endDate\n" +
                        "execute_script - выполнить скрипт\n" +
                        "exit - завершить программу"
        );
        helpText.setForeground(Color.WHITE);
        helpText.setBackground(Color.BLACK);
        helpText.setFont(new Font("Monospaced", Font.PLAIN, 16));
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setOpaque(false);
        helpText.setColumns(40);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 30, 20);
        add(helpText, gbc);

        JButton backButton = new JButton("ВЕРНУТЬСЯ");
        backButton.setBackground(new Color(48, 48, 48));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 25));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> parent.showMainApp(currentUser));
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 50, 0);
        add(backButton, gbc);
    }
}