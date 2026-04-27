package edu.itmo.piikt.server;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.manager.Network;
import java.io.IOException;

/**
 * Initializes the server, loads data from CSV file, starts the network server,
 * and handles graceful shutdown with data persistence
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class MainServer {
	private static final AppLogger logger = new AppLogger(MainServer.class);

	/**
	 * Application entry point
	 *
	 * @param args
	 *            command line arguments for logging configuration
	 */
	public static void main(String[] args) {
		Config.configureFromArgs(args);

		try (Context ignored = Context.newId()) {
			logger.info("Starting server");
			logger.info("Data loaded from file");
			Dispatcher dispatcher = new Dispatcher();
			Network netWork = new Network(dispatcher);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try (Context ignored1 = Context.newId()) {
					logger.info("Shutdown signal received");
					netWork.stop();
					logger.info("Server stopped");
				}
			}));

			try {
				netWork.start();
			} catch (IOException e) {
				logger.error("Failed to start server: {}", e.getMessage());
				e.printStackTrace();
			}

		} catch (Exception e) {
			logger.error("Server initialization failed: {}", e.getMessage());
			e.printStackTrace();
		}
	}
}
