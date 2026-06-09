package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.bd.SearchWorkerList;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.CollectionManager;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.manager.Websocket;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 5.1
 */
@NoArgsConstructor
public final class RemoveLowerCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(RemoveLowerCommand.class);
	private FirestoreService firestore;
	private CollectionManager collectionManager;
	private Websocket wsServer;

	public void setCollectionManager(CollectionManager collectionManager) {
		this.collectionManager = collectionManager;
	}

	public void setWebSocketServer(Websocket wsServer) {
		this.wsServer = wsServer;
	}

	private FirestoreService getFirestore() {
		if (firestore == null) {
			try {
				firestore = new FirestoreService();
			} catch (IOException e) {
				logger.error("Failed to initialize Firestore: {}", e.getMessage(), e);
			}
		}
		return firestore;
	}

	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("Command unavailable, please try again later");
			}
			String argument = clientCommand.argumentCommand();
			logger.info("Executing REMOVE_LOWER with argument: {}", argument);

			if (argument == null || argument.trim().isEmpty()) {
				return ServerResponse.error("Дата не введена");
			}

			LocalDate date;
			try {
				date = LocalDate.parse(argument.trim());
			} catch (DateTimeParseException e) {
				return ServerResponse.error("Неверный формат даты");
			}

			if (collectionManager == null) {
				return ServerResponse.error("Collection manager not available");
			}

			var listWorker = collectionManager.getAllWorkers();
			List<Worker> listRemove = listWorker.stream()
					.filter(worker -> worker.getStartDate() != null && worker.getStartDate().isAfter(date)).toList();

			List<Worker> listEnd = SearchWorkerList.searchWorkerList(clientCommand, listRemove);

			for (Worker worker : listEnd) {
				ServerResponse serverResponse = getFirestore().deleteWorker(worker.getUuid());
				if (serverResponse != null && serverResponse.exception()) {
					return serverResponse;
				}
			}

			for (Worker worker : listEnd) {
				collectionManager.removeWorker(worker.getUuid());
			}
			if (wsServer != null && !listEnd.isEmpty()) {
				wsServer.broadcastUpdate("CLEAR", null);
			}
			int removed = listEnd.size();
			logger.info("Removed {} workers with start date after {}", removed, date);
			return ServerResponse.successfulCompletion("REMOVE LOWER");
		} catch (Exception e) {
			logger.error("Error executing REMOVE_LOWER: {}", e);
			return ServerResponse.error("Internal server error: " + e.getMessage());
		}
	}
}
