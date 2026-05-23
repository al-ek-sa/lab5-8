package edu.itmo.piikt.client.gui.ss;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainAppPanel extends JPanel {

    public MainAppPanel() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        welcomeLabel.setText("<html><body style='text-align: center; width: 500px;'>" +
                "Вы вошли в аккаунт!<br><br>" +
                "Для поиска информации о командах воспользуйтесь командой помощь.<br>" +
                "Для этого необходимо перейти в меню обозначенное как ☰ и выбрать команду." +
                "</body></html>");

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(welcomeLabel, gbc);
        ImageIcon gearIcon = loadImage("images/gear2.png");
        if (gearIcon != null) {
            Image img = gearIcon.getImage();
            Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            add(imageLabel, gbc);
        }
    }

    private ImageIcon loadImage(String path) {
        try {
            URL imgUrl = getClass().getClassLoader().getResource(path);
            if (imgUrl != null) {
                return new ImageIcon(imgUrl);
            }
        } catch (Exception e) {
            System.err.println("Image not found: " + path);
        }
        return null;
    }
}