package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SearchResultPanel extends JPanel {
    private MainAppPanel parent;
    private String currentUser;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchResultPanel(MainAppPanel parent, String username) {
        this.parent = parent;
        this.currentUser = username;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("РЕЗУЛЬТАТЫ ПОИСКА");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        String[] columnNames = {
                "id", "Имя", "Координата X", "Координата Y", "Зарплата",
                "Дата начала работы", "Дата окончания работы", "Статус",
                "Годовой оборот", "Тип организации", "Адрес организации"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setBackground(new Color(200, 200, 210));
        resultTable.setForeground(Color.BLACK);
        resultTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultTable.setRowHeight(30);
        resultTable.setGridColor(new Color(180, 180, 190));
        resultTable.setSelectionBackground(new Color(150, 150, 160));
        resultTable.setSelectionForeground(Color.BLACK);
        resultTable.setShowHorizontalLines(true);
        resultTable.setShowVerticalLines(false);
        resultTable.setBorder(null);
        JTableHeader header = resultTable.getTableHeader();
        header.setBackground(new Color(180, 180, 190));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBorder(null);
        header.setReorderingAllowed(true);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.CENTER);
        addTestData();
    }

    private void addTestData() {
        Object[] row1 = {
                1, "Иван Иванов", 5, 10, 50000,
                "2023-01-15", "2024-01-15", "HIRED",
                1000000, "COMMERCIAL", "ул. Ленина, 10"
        };
        Object[] row2 = {
                2, "Петр Петров", 3, -5, 75000,
                "2022-06-01", null, "PROBATION",
                2500000, "GOVERNMENT", "пр. Мира, 25"
        };
        Object[] row3 = {
                3, "Сидор Сидоров", 8, 15, 60000,
                "2023-09-10", "2025-09-10", "FIRED",
                500000, "TRUST", "ул. Садовая, 7"
        };
        Object[] row4 = {
                4, "Анна Смирнова", 2, 20, 90000,
                "2021-03-20", null, "RECOMMENDED_FOR_PROMOTION",
                5000000, "OPEN_JOINT_STOCK_COMPANY", "б-р. Победы, 1"
        };

        tableModel.addRow(row1);
        tableModel.addRow(row2);
        tableModel.addRow(row3);
        tableModel.addRow(row4);
    }
}