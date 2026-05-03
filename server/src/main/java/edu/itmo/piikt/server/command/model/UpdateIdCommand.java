package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.bd.WorkerAdd;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.validation.object.BuilderWorker;
import edu.itmo.piikt.server.validation.object.ValidationError;
import edu.itmo.piikt.server.validation.object.WorkerBuilder;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * The class implements the command update id {element} : update the value of
 * the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class UpdateIdCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(AddCommand.class);
	private final BuilderWorker builderWorker = new BuilderWorker();
	private final WorkerBuilder workerBuilder = new WorkerBuilder();
	private final WorkerAdd workerAdd = new WorkerAdd();
	private FirestoreService firestore;

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
				logger.warn("Database not connected");
				return ServerResponse.error("на данный момент, команда не доступна, повторите попытку позже");
			}

			String targetId = clientCommand.getArgumentCommand();
			logger.info("UPDATE command started for id: {}", targetId);

			if (targetId == null || targetId.trim().isEmpty()) {
				logger.warn("Empty worker id provided");
				return ServerResponse.error("Worker ID cannot be empty");
			}

			WorkerData dataWorker = (WorkerData) clientCommand.getData();
			logger.debug("Worker data received: name={}, salary={}", dataWorker.getName(), dataWorker.getSalary());

			Object result = builderWorker.data(dataWorker);
			if (result instanceof WorkerData) {
				Worker worker = workerBuilder.builerWorker(dataWorker);
				worker.setUuid(clientCommand.getArgumentCommand());
				logger.info("Worker UUID set to: {}", worker.getUuid());

				ServerResponse serverResponse = workerAdd.newWorker(clientCommand, worker);
				if (serverResponse.execution()) {
					logger.info("PostgreSQL save successful for id: {}", worker.getUuid());

					FirestoreService fs = getFirestore();
					if (fs != null) {
						ServerResponse saved = fs.saveWorker(worker);
						if (saved.execution()) {
							logger.info("Firestore save successful for id: {}", worker.getUuid());
							HistoryWorker.INSTANCE.add(worker);
							logger.info("Worker added to memory. Total workers: {}",
									HistoryWorker.INSTANCE.getListWorker().size());
						} else {
							logger.warn("Firestore save failed for id: {}", worker.getUuid());
						}
					}
					logger.info("UPDATE command completed successfully for id: {}", targetId);
					return ServerResponse.successfulCompletion("UPDATE");
				} else {
					logger.error("PostgreSQL save failed for id: {}", targetId);
					return serverResponse;
				}
			} else if (result instanceof ValidationError(List<MessageExceptionValidation>errors,Object data)) {
				logger.warn("Validation failed: {} errors", errors.size());
				return ServerResponse.error("Incorrect data entered", errors, data);
			}

			logger.error("Unknown validation result type: {}", result.getClass().getName());
			return ServerResponse.error("Internal server error while processing UPDATE command");
		} catch (Exception e) {
			logger.error("Error executing UPDATE command: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
