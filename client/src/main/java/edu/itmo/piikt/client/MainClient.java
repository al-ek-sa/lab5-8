package edu.itmo.piikt.client;

import static java.lang.Thread.sleep;

import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.providerType.IOConsole;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;

/**
 * Initializes the client, establishes connection to the server,
 * and starts the command processing loop. Handles reconnection attempts
 * and graceful shutdown
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class MainClient {
	private static final AppLogger logger = new AppLogger(MainClient.class);
	/** Delay between reconnection attempts in milliseconds. */
	private static final int TIME = 5000;
	public static void main(String[] args) {
		Config.configureFromArgs(args);

		try (Context ignored = Context.newId()) {
			logger.info("Starting client");
			IOProvider io = new IOConsole();
			Network client;

			// Infinite connection attempts
			while (true){
				try {
					logger.info("Attempting to connect to server");
					Network network = new Network();
					network.connect();
					client = network;
					logger.info("Connected to server");
					break;
				} catch (Exception e) {
					logger.warn("Connection failed: {}", e.getMessage());
					try {
						sleep(TIME);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						logger.error("Interrupted while waiting to reconnect");
						return;
					}
				}
			}

			// Start command processing loop
			try {
				ValidationCommand.INSTANCE.setNetwork(client);
				ValidationCommand.INSTANCE.validation(io);
			} catch (Exception e) {
				logger.error("Error in command processing: {}", e.getMessage());
			} finally {
				// Close connection
				try {
                    client.close();
					logger.info("Client stopped");
				} catch (Exception e) {
					logger.error("Error closing connection: {}", e.getMessage());
				}
			}
		}
	}
}
