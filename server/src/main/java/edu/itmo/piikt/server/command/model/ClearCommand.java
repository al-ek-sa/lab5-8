package edu.itmo.piikt.server.command.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.bd.SearchWorker;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.CollectionManager;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.manager.Websocket;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class ClearCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(ClearCommand.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	private FirestoreService firestore;
	@Setter
	private CollectionManager collectionManager;
	private Websocket wsServer;

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

			String currentUser = clientCommand.user();
			logger.info("Executing CLEAR command for user: {}", currentUser);

			if (collectionManager == null) {
				return ServerResponse.error("Collection manager not available");
			}
			List<Worker> allWorkers = collectionManager.getAllWorkers();
			List<Worker> userWorkers = new ArrayList<>();
			for (Worker worker : allWorkers) {
				ServerResponse check = SearchWorker.getWorkerIdsByUserId(clientCommand, worker.getUuid());
				if (check.execution()) {
					userWorkers.add(worker);
				}
			}

			if (userWorkers.isEmpty()) {
				logger.info("No workers found for user: {}", currentUser);
				return ServerResponse.successfulCompletion("CLEAR: no workers to remove");
			}

			logger.info("Found {} workers to delete for user {}", userWorkers.size(), currentUser);
			for (Worker worker : userWorkers) {
				ServerResponse response = getFirestore().deleteWorker(worker.getUuid());
				if (response != null && response.exception()) {
					logger.warn("Failed to delete worker {} from Firestore", worker.getUuid());
				}
			}
			for (Worker worker : userWorkers) {
				if (collectionManager != null) {
					collectionManager.removeWorker(worker.getUuid());
				}
			}
			if (wsServer != null) {
				ObjectNode data = objectMapper.createObjectNode();
				data.put("type", "CLEAR");
				data.put("user", currentUser);
				wsServer.broadcastUpdate("CLEAR", null);
			}

			logger.info("User {} cleared {} workers", currentUser, userWorkers.size());
			return ServerResponse.successfulCompletion("CLEAR");

		} catch (Exception e) {
			logger.error("Error executing CLEAR command: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error: " + e.getMessage());
		}
	}
}
