package edu.itmo.piikt.client;

import edu.itmo.piikt.client.entrance.Registr;
import edu.itmo.piikt.client.mode.InteractiveMode;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.providerType.IOConsole;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;

import java.io.IOException;

/**
 * Initializes the client, establishes connection to the server, and starts the
 * command processing loop. Handles reconnection attempts and graceful shutdown
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class MainClient {
	private static final AppLogger logger = new AppLogger(MainClient.class);
	public static void main(String[] args) {
		Config.configureFromArgs(args);

		try (Context ignored = Context.newId()) {
			logger.info("Starting client");
			Network client = new Network();
			client.connect();
			IOProvider io = new IOConsole();
			Registr registr = new Registr(io);
			registr.registration(client);
			InteractiveMode interactiveMode = new InteractiveMode();
			interactiveMode.execute(client, io, registr);
			client.close();
		} catch (IOException e) {
			logger.error("Client failed: {}", e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
