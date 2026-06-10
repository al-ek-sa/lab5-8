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
import edu.itmo.piikt.server.validation.object.BuilderWorker;
import edu.itmo.piikt.server.validation.object.ValidationError;
import edu.itmo.piikt.server.validation.object.WorkerBuilder;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.command.bd.WorkerAdd;
import lombok.Setter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public final class AddCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(AddCommand.class);
	private final BuilderWorker builderWorker = new BuilderWorker();
	private final WorkerBuilder workerBuilder = new WorkerBuilder();
	private final ObjectMapper objectMapper;
	private FirestoreService firestore;
	@Setter
	private CollectionManager collectionManager;
	private Websocket wsServer;
	private final WorkerAdd workerAdd = new WorkerAdd();

	{
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
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
			logger.info("Executing ADD command for user: {}", clientCommand.user());

			Object dataObj = clientCommand.data();
			WorkerData dataWorker;
			if (dataObj instanceof Map) {
				dataWorker = objectMapper.convertValue(dataObj, WorkerData.class);
			} else if (dataObj instanceof WorkerData) {
				dataWorker = (WorkerData) dataObj;
			} else {
				return ServerResponse.error("Invalid data type. Expected WorkerData");
			}

			if (dataWorker == null) {
				return ServerResponse.error("Failed to parse WorkerData");
			}

			Object result = builderWorker.data(dataWorker);
			if (result instanceof WorkerData) {
				Worker worker = workerBuilder.builerWorker(dataWorker);

				if (worker.getUuid() == null || worker.getUuid().isEmpty()) {
					worker.setUuid(UUID.randomUUID().toString());
				}

				ServerResponse dbResponse = workerAdd.newWorker(clientCommand, worker);
				if (!dbResponse.execution()) {
					logger.error("Failed to save worker to PostgreSQL: {}", dbResponse.message());
					return ServerResponse.error("Failed to save to database: " + dbResponse.message());
				}
				logger.info("Worker saved to PostgreSQL: id={}", worker.getUuid());

				FirestoreService fs = getFirestore();
				if (fs != null) {
					ServerResponse saved = fs.saveWorker(worker);
					if (saved.execution()) {
						if (collectionManager != null) {
							collectionManager.addWorker(worker);
						}
						if (wsServer != null) {
							ObjectNode data = objectMapper.valueToTree(worker);
							wsServer.broadcastUpdate("ADD", data);
						}
						return ServerResponse.successfulCompletion("ADD");
					} else {
						logger.error("Failed to save to Firestore, but PostgreSQL saved: {}", saved.message());
						return ServerResponse.error("Failed to save to Firestore: " + saved.message());
					}
				} else {
					if (collectionManager != null) {
						collectionManager.addWorker(worker);
					}
					if (wsServer != null) {
						ObjectNode data = objectMapper.valueToTree(worker);
						wsServer.broadcastUpdate("ADD", data);
					}
					return ServerResponse.successfulCompletion("ADD");
				}
			} else if (result instanceof ValidationError) {
				return ServerResponse.error("Incorrect data entered");
			}
			return ServerResponse.error("Internal server error while processing ADD command");
		} catch (Exception e) {
			logger.error("Error executing ADD command: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error: " + e.getMessage());
		}
	}
}
