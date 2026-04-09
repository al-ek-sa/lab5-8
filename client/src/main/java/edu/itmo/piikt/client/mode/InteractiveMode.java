package edu.itmo.piikt.client.mode;

import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;

import static java.lang.Thread.sleep;

public class InteractiveMode implements ClientMode {

	private static final AppLogger logger = new AppLogger(InteractiveMode.class);
	private static final int RECONNECT = 500;

	@Override
	public String getName() {
		return NameMode.INTERACTIVE.getName();
	}

	@Override
	public void execute(Network existingNetwork, IOProvider inputProvider) {
		try (Context ignored = Context.newId()) {
			logger.info("Starting interactive mode");

			Network client = existingNetwork;

			if (client == null || !client.connected()) {
				client = connectToServer();
				if (client == null) {
					logger.error("Failed to connect to server");
					return;
				}
			}

			// Start command processing loop
			try {
				ValidationCommand.INSTANCE.setNetwork(client);
				ValidationCommand.INSTANCE.validation(inputProvider);
			} catch (Exception e) {
				logger.error("Error in command processing: {}", e.getMessage());
			} finally {
				try {
					// Close connection
					client.close();
					logger.info("Client stopped");
				} catch (Exception e) {
					logger.error("Error closing connection: {}", e.getMessage());
				}
			}
		}
	}

	private Network connectToServer() {
		while (true) {
			try {
				logger.info("Attempting to connect to server");
				Network client = new Network();
				client.connect();
				logger.info("Connected to server");
				return client;
			} catch (Exception e) {
				logger.warn("Connection failed: {}", e.getMessage());
				try {
					sleep(RECONNECT);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					logger.error("Interrupted while waiting to reconnect");
					return null;
				}
			}
		}
	}
}
