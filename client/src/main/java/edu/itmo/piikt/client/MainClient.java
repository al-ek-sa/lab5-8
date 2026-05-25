package edu.itmo.piikt.client;

import edu.itmo.piikt.client.gui.MainGUI;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;

import javax.swing.*;

/**
 * Initializes the client and starts the GUI.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 */
public class MainClient {
	private static final AppLogger logger = new AppLogger(MainClient.class);

	public static void main(String[] args) {
		Config.configureFromArgs(args);

		SwingUtilities.invokeLater(() -> {
			try (Context ignored = Context.newId()) {
				logger.info("Starting client GUI");
				MainGUI mainGUI = new MainGUI();
				mainGUI.setVisible(true);
				logger.info("Client GUI started successfully");
			} catch (Exception e) {
				logger.error("Client failed: {}", e.getMessage());
				JOptionPane.showMessageDialog(null, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
