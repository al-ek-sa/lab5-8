package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {

    public TopPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 200));
        setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("WORKERFLOW");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        add(titleLabel, BorderLayout.WEST);

        add(createLanguageButton(), BorderLayout.EAST);
    }

    private JPanel createLanguageButton() {
        JButton langButton = new JButton("RU");
        langButton.setFont(new Font("Arial", Font.PLAIN, 14));
        langButton.setFocusPainted(false);
        langButton.setBackground(new Color(100, 150, 220));
        langButton.setForeground(Color.WHITE);
        langButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu langMenu = new JPopupMenu();
        langMenu.add(new JMenuItem("Русский"));
        langMenu.add(new JMenuItem("Deutsch"));
        langMenu.add(new JMenuItem("Svenska"));
        langMenu.add(new JMenuItem("Español"));

        langButton.addActionListener(e -> langMenu.show(langButton, 0, langButton.getHeight()));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(langButton);

        return rightPanel;
    }
}