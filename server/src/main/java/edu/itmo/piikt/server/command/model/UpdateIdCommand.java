package edu.itmo.piikt.server.command.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.manager.CollectionManager;
import edu.itmo.piikt.server.manager.Websocket;
import edu.itmo.piikt.server.validation.object.WorkerBuilder;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@NoArgsConstructor
public final class UpdateIdCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(UpdateIdCommand.class);
	private final WorkerBuilder workerBuilder = new WorkerBuilder();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private FirestoreService firestore;
	private CollectionManager collectionManager;
	private Websocket wsServer;

	{
		objectMapper.registerModule(new JavaTimeModule());
	}

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
			String targetId = clientCommand.argumentCommand();
			if (targetId == null || targetId.trim().isEmpty()) {
				return ServerResponse.error("Worker ID cannot be empty");
			}

			Object dataObj = clientCommand.data();
			WorkerData dataWorker;

			if (dataObj instanceof Map) {
				dataWorker = objectMapper.convertValue(dataObj, WorkerData.class);
			} else if (dataObj instanceof WorkerData) {
				dataWorker = (WorkerData) dataObj;
			} else {
				return ServerResponse.error("Invalid data type");
			}

			if (dataWorker == null) {
				return ServerResponse.error("Failed to parse WorkerData");
			}

			Worker worker = workerBuilder.builerWorker(dataWorker);
			worker.setUuid(targetId);

			FirestoreService fs = getFirestore();
			if (fs != null) {
				ServerResponse saved = fs.saveWorker(worker);
				if (saved.execution()) {
					if (collectionManager != null) {
						collectionManager.updateWorker(worker);
					}
					if (wsServer != null) {
						ObjectNode data = objectMapper.valueToTree(worker);
						wsServer.broadcastUpdate("UPDATE", data);
					}
					return ServerResponse.successfulCompletion("UPDATE");
				} else {
					return ServerResponse.error("Failed to save to Firestore: " + saved.message());
				}
			} else {
				if (collectionManager != null) {
					collectionManager.updateWorker(worker);
				}
				if (wsServer != null) {
					ObjectNode data = objectMapper.valueToTree(worker);
					wsServer.broadcastUpdate("UPDATE", data);
				}
				return ServerResponse.successfulCompletion("UPDATE");
			}
		} catch (Exception e) {
			logger.error("Error executing UPDATE command: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error: " + e.getMessage());
		}
	}
}
