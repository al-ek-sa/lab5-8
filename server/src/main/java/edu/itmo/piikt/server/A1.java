package edu.itmo.piikt.server;

import java.awt.*;
import javax.swing.*;

public class A1 {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JPanel contentPane = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int rectWidth = 150;
					int rectHeight = 295;
					Color lightGray = new Color(169, 169, 169, 255);
					g2d.setColor(lightGray);
					drawRotatedRect(g2d, 300, 364, rectWidth, rectHeight, 0);
					drawRotatedRect(g2d, 82, -65, rectWidth, rectHeight, -60);
					drawRotatedRect(g2d, 82, 795, rectWidth, rectHeight, 60);
					int centerX = -68;
					int centerY = 450;
					int whiteRadius = 450;
					g2d.setColor(Color.WHITE);
					g2d.fillOval(centerX - whiteRadius, 0, whiteRadius * 2, whiteRadius * 2);
					int blackRadius = 256;
					g2d.setColor(Color.BLACK);
					g2d.fillOval(centerX - blackRadius, centerY - blackRadius, blackRadius * 2, blackRadius * 2);
				}

				private void drawRotatedRect(Graphics2D g2d, int x, int y, int width, int height, double angleDegrees) {
					Graphics2D g2dRotated = (Graphics2D) g2d.create();
					double angleRadians = Math.toRadians(angleDegrees);
					int centerX = x + width / 2;
					int centerY = y + height / 2;
					g2dRotated.rotate(angleRadians, centerX, centerY);
					g2dRotated.fillRect(x, y, width, height);
					g2dRotated.dispose();
				}
			};
			contentPane.setRequestFocusEnabled(true);
			contentPane.setLayout(null);
			contentPane.setBackground(new Color(0, 0, 0));
			contentPane.setPreferredSize(new Dimension(1440, 900));
			Font arialBase = new Font("Arial", Font.PLAIN, 1);
			Font arialMedium = arialBase.deriveFont(Font.PLAIN, 90f);
			Font buttonFont = arialBase.deriveFont(Font.PLAIN, 36f);
			JLabel label1 = new JLabel("WORKERFLOW");
			label1.setFont(arialMedium);
			label1.setForeground(Color.WHITE);
			label1.setBounds(661, 233, 680, 95);
			contentPane.add(label1);
			JButton loginButton = new JButton("ВХОД В АККАУНТ");
			loginButton.setFont(buttonFont);
			loginButton.setForeground(Color.WHITE);
			loginButton.setBackground(new Color(64, 64, 64));
			loginButton.setFocusPainted(false);
			loginButton.setBorderPainted(false);
			loginButton.setBounds(661, 414, 680, 95);
			contentPane.add(loginButton);
			JButton registerButton = new JButton("РЕГИСТРАЦИЯ");
			registerButton.setFont(buttonFont);
			registerButton.setForeground(Color.WHITE);
			registerButton.setBackground(new Color(64, 64, 64));
			registerButton.setFocusPainted(false);
			registerButton.setBorderPainted(false);
			registerButton.setBounds(661, 539, 680, 95);
			contentPane.add(registerButton);
			JFrame frame = new JFrame("Мое окно");
			frame.setContentPane(contentPane);
			frame.pack();
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}
