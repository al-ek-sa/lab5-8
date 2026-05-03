package edu.itmo.piikt.server;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.providerType.IOConsole;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.server.command.server.CommandConsole;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.manager.Network;
import edu.itmo.piikt.server.registration.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static java.lang.Thread.sleep;

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
			Thread.startVirtualThread(() -> {
				IOProvider io = new IOConsole();
				CommandConsole.INSTANCE.execute(io);
			});
			Thread.startVirtualThread(() -> {
				while (true) {
					if (!BDConnect.INSTANCE.isConnected()) {
						try {
							BDConnect.INSTANCE.connection();
						} catch (SQLException | InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});
			logger.info("Starting server");
			logger.info("Data loaded from file");
			try {
				FirestoreService firestore = new FirestoreService();
				List<Worker> cloudWorkers = firestore.getAllWorkers();
				for (Worker w : cloudWorkers) {
					HistoryWorker.INSTANCE.add(w);
				}
				logger.info("Loaded {} workers from Firestore into memory", cloudWorkers.size());
			} catch (Exception e) {
				logger.error("Failed to load workers from Firestore: {}", e.getMessage(), e);
			}
			Dispatcher dispatcher = new Dispatcher();
			User user = new User();
			Network netWork = new Network(dispatcher, user);
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
			}

		} catch (Exception e) {
			logger.error("Server initialization failed: {}", e.getMessage());
		}
	}
}
