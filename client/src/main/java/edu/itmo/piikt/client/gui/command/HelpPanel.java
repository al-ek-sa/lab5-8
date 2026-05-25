package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
@EqualsAndHashCode(callSuper = true)
@Data
public class HelpPanel extends JPanel {
	private MainAppPanel parent;
	private String currentUser;

	public HelpPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		JLabel titleLabel = new JLabel("ПОМОЩЬ");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 70));
		gbc.insets = new Insets(60, 0, 30, 0);
		add(titleLabel, gbc);
		JTextArea helpText = new JTextArea();
		helpText.setText("""
                help - вывести справку
                info - информация о коллекции
                show - показать все элементы
                add - добавить элемент
                update - обновить элемент
                remove_by_id - удалить по id
                clear - очистить коллекцию
                head - первый элемент
                history - история команд
                count_by_organization - количество по организации
                filter_contains_name - фильтр по имени
                print_field_desc_end_date - вывести endDate
                execute_script - выполнить скрипт
                exit - завершить программу""");
		helpText.setForeground(Color.WHITE);
		helpText.setBackground(Color.BLACK);
		helpText.setFont(new Font("Monospaced", Font.PLAIN, 18));
		helpText.setEditable(false);
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		helpText.setOpaque(false);
		helpText.setColumns(35);

		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(helpText, gbc);
	}
}
