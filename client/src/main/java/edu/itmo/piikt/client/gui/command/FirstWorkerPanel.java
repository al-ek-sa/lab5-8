package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.ss.MainAppPanel;

import javax.swing.*;
import java.awt.*;

public class FirstWorkerPanel extends JPanel {
    private MainAppPanel parent;
    private String currentUser;

    public FirstWorkerPanel(MainAppPanel parent, String username) {
        this.parent = parent;
        this.currentUser = username;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JLabel titleLabel = new JLabel("ПЕРВЫЙ РАБОТНИК");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titleLabel, gbc);

        JLabel firstWorkerText = new JLabel("Первый работник будет отображаться здесь");
        firstWorkerText.setForeground(Color.WHITE);
        firstWorkerText.setFont(new Font("Arial", Font.PLAIN, 20));
        firstWorkerText.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(firstWorkerText, gbc);
    }
}