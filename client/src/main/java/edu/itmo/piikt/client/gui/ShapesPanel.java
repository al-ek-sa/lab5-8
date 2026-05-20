package edu.itmo.piikt.client.gui;

import javax.swing.*;
import java.awt.*;

public class ShapesPanel extends JPanel {

    public ShapesPanel() {
        setBackground(new Color(240, 248, 255));
        setPreferredSize(new Dimension(400, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int centerX = w / 2;
        int centerY = h / 2;

        drawCircle(g2d, centerX - 100, centerY - 80, 80, new Color(70, 130, 200, 180));
        drawCircle(g2d, centerX - 30, centerY - 40, 60, new Color(255, 140, 0, 180));

        drawRect(g2d, centerX + 30, centerY - 70, 70, 50, new Color(100, 200, 100, 200), 0);
        drawRotatedRect(g2d, centerX + 60, centerY - 20, 70, 50, new Color(200, 100, 100, 200), 45);
        drawRotatedRect(g2d, centerX + 40, centerY + 60, 80, 50, new Color(150, 100, 200, 200), -30);
    }

    private void drawCircle(Graphics2D g2d, int x, int y, int size, Color color) {
        g2d.setColor(color);
        g2d.fillOval(x, y, size, size);
    }

    private void drawRect(Graphics2D g2d, int x, int y, int w, int h, Color color, double angle) {
        g2d.setColor(color);
        g2d.fillRect(x, y, w, h);
    }

    private void drawRotatedRect(Graphics2D g2d, int x, int y, int w, int h, Color color, double angle) {
        Graphics2D rotated = (Graphics2D) g2d.create();
        rotated.setColor(color);
        rotated.rotate(Math.toRadians(angle), x + w / 2, y + h / 2);
        rotated.fillRect(x, y, w, h);
        rotated.dispose();
    }
}