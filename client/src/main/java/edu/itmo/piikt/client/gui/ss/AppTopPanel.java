package edu.itmo.piikt.client.gui.ss;

import edu.itmo.piikt.client.gui.MainGUI;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppTopPanel extends JPanel {
    private final MainGUI parent;
    private final JLabel userLabel;
    private final JButton langButton;
    private final JButton logoutButton;
    private String currentUser;
    private MainAppPanel mainAppPanel;

    public AppTopPanel(MainGUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftPanel.setOpaque(false);

        JButton menuButton = new JButton("☰");
        menuButton.setFont(new Font("Arial", Font.BOLD, 24));
        menuButton.setFocusPainted(false);
        menuButton.setBackground(new Color(20, 20, 30));
        menuButton.setForeground(Color.WHITE);
        menuButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu commandMenu = new JPopupMenu();
        commandMenu.add(createCommandMenuItem("search_by_organization", "ПОИСК ПО ОРГАНИЗАЦИИ"));
        commandMenu.add(createCommandMenuItem("help", "ПОМОЩЬ"));
        commandMenu.add(createCommandMenuItem("info", "ИНФОРМАЦИЯ"));
        commandMenu.add(createCommandMenuItem("history", "ИСТОРИЯ"));
        commandMenu.add(createCommandMenuItem("animation", "АНИМАЦИЯ"));
        commandMenu.add(createCommandMenuItem("first_worker", "ПЕРВЫЙ РАБОТНИК"));
        commandMenu.add(createCommandMenuItem("read_file", "ЧТЕНИЕ ИЗ ФАЙЛА"));
        commandMenu.add(createCommandMenuItem("show", "ПОКАЗ"));

        menuButton.addActionListener(e -> commandMenu.show(menuButton, 0, menuButton.getHeight()));
        leftPanel.add(menuButton);

        JLabel titleLabel = new JLabel("WORKERFLOW");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        leftPanel.add(titleLabel);

        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        buttonPanel.setOpaque(false);

        userLabel = new JLabel("");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        buttonPanel.add(userLabel);

        langButton = new JButton("RU");
        langButton.setFont(new Font("Arial", Font.BOLD, 12));
        langButton.setFocusPainted(false);
        langButton.setBackground(new Color(60, 60, 70));
        langButton.setForeground(Color.WHITE);
        langButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        langButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu langMenu = new JPopupMenu();
        langMenu.add(createLangMenuItem("Русский", "RU"));
        langMenu.add(createLangMenuItem("Deutsch", "DE"));
        langMenu.add(createLangMenuItem("Svenska", "SV"));
        langMenu.add(createLangMenuItem("Español", "ES"));
        langButton.addActionListener(e -> langMenu.show(langButton, 0, langButton.getHeight()));
        buttonPanel.add(langButton);

        logoutButton = new JButton("->");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(60, 60, 70));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });
        buttonPanel.add(logoutButton);

        rightPanel.add(buttonPanel, BorderLayout.EAST);
        add(rightPanel, BorderLayout.EAST);
    }

    private JMenuItem createCommandMenuItem(String command, String description) {
        JMenuItem item = new JMenuItem(description);
        item.addActionListener(e -> {
            System.out.println("Command: " + command);
            System.out.println("currentUser value: '" + currentUser + "'");

            if (command.equals("help")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showHelp(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (command.equals("info")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showInfo(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (command.equals("history")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showHistory(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (command.equals("first_worker")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showFirstWorker(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (command.equals("read_file")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showReadFile(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (command.equals("search_by_organization")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showSearchByOrganization(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (command.equals("show")) {
                if (mainAppPanel != null) {
                    mainAppPanel.showShow(currentUser);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Панель приложения не инициализирована",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Команда '" + description + "' будет реализована позже",
                        "Информация",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return item;
    }

    private JMenuItem createLangMenuItem(String langName, String code) {
        JMenuItem item = new JMenuItem(langName + " (" + code + ")");
        item.addActionListener(e -> {
            langButton.setText(code);
        });
        return item;
    }

    public void setUsername(String username) {
        System.out.println("setUsername called with: '" + username + "'");
        this.currentUser = username;
        String displayName = username;
        if (displayName.length() > 15) {
            displayName = displayName.substring(0, 12) + "...";
        }
        userLabel.setText(displayName);
    }
}