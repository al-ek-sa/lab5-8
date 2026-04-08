package edu.itmo.piikt.client;

import static java.lang.Thread.sleep;

import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.providerType.IOConsole;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;

public class MainClient {
	private static final AppLogger logger = new AppLogger(MainClient.class);
	private static final int MAX_ATTEMPTS = 7;

	public static void main(String[] args) {
		Config.configureFromArgs(args);

		try (Context ignored = Context.newId()) {
			logger.info("Starting client...");
			IOProvider io = new IOConsole();
			Network client = null;

			for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
				try {
					logger.debug("Connection attempt {}/{}", attempt, MAX_ATTEMPTS);
					Network network = new Network();
					network.connect();
					client = network;
					logger.info("Connected to server");
					break;
				} catch (Exception e) {
					logger.warn("Connection attempt {} failed: {}", attempt, e.getMessage());
					if (attempt == MAX_ATTEMPTS) {
						logger.error("Max connection attempts reached");
						return;
					}
					try {
						sleep(5000);
					} catch (InterruptedException ex) {
						logger.error("Interrupted while waiting to reconnect");
						return;
					}
				}
			}

			try {
				ValidationCommand.INSTANCE.setNetwork(client);
				ValidationCommand.INSTANCE.validation(io);
			} catch (Exception e) {
				logger.error("Error in command processing: {}", e.getMessage());
			} finally {
				try {
					assert client != null;
					client.close();
					logger.info("Client stopped");
				} catch (Exception e) {
					logger.error("Error closing connection: {}", e.getMessage());
				}
			}
		}
	}
}
