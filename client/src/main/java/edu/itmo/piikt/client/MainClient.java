package edu.itmo.piikt.client;

import edu.itmo.piikt.client.manager.CronValidationCommand;
import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.client.mode.CronMode;
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
			if (args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					if (args[i].equalsIgnoreCase("--cron") && i + 1 < args.length) {
						String command = args[i + 1];
						CronMode cronMode = new CronMode(command);
						cronMode.execute(client, io);
						client.close();
						return;
					} else if (args[i].equalsIgnoreCase("--cron")) {
						client.close();
						return;
					}
				}
			}
			ValidationCommand.INSTANCE.setNetwork(client);
			ValidationCommand.INSTANCE.validation(io);
			client.close();
		} catch (IOException e) {
			logger.error("Client failed: {}", e.getMessage());
		}
	}
}
