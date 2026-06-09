package edu.itmo.piikt.server.command.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.bd.SearchWorker;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.CollectionManager;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.manager.Websocket;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public final class RemoveByIdCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(RemoveByIdCommand.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
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
			String id = clientCommand.argumentCommand();
			if (id == null || id.trim().isEmpty()) {
				return ServerResponse.error("ID не введено");
			}
			ServerResponse serverResponse = SearchWorker.getWorkerIdsByUserId(clientCommand, id);
			if (serverResponse.execution()) {
				return serverResponse;
			}
			serverResponse = getFirestore().deleteWorker(id);
			if (serverResponse.exception()) {
				return serverResponse;
			}

			if (collectionManager != null) {
				collectionManager.removeWorker(id);
			}
			if (wsServer != null) {
				ObjectNode data = objectMapper.createObjectNode();
				data.put("uuid", id);
				wsServer.broadcastUpdate("REMOVE", data);
			}
			return ServerResponse.successfulCompletion("REMOVE BY ID");
		} catch (Exception e) {
			logger.error("Error executing REMOVE_BY_ID: {}", e);
			return ServerResponse.error("Internal server error: " + e.getMessage());
		}
	}
}
