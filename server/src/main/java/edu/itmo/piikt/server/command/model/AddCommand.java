package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.manager.FirestoreService;
import edu.itmo.piikt.server.validation.object.BuilderWorker;
import edu.itmo.piikt.server.validation.object.ValidationError;
import edu.itmo.piikt.server.validation.object.WorkerBuilder;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.command.bd.WorkerAdd;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * The class implements the command add {element} : add a new element to the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 4.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class AddCommand implements CommandType {
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

	/**
	 * Executes the ADD command
	 *
	 * @param clientCommand
	 *            command containing WorkerData
	 * @return with success or error information
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse
						.error("         return ServerResponse.error(\"Command unavailable, please try again later");
			}
			logger.info("Executing ADD command");
			WorkerData dataWorker = (WorkerData) clientCommand.data();
			logger.debug("Worker data received: name={}, salary={}", dataWorker.getName(), dataWorker.getSalary());
			// Validate data
			Object result = builderWorker.data(dataWorker);
			if (result instanceof WorkerData) {
				Worker worker = workerBuilder.builerWorker(dataWorker);
				ServerResponse serverResponse = workerAdd.newWorker(clientCommand, worker);
				if (serverResponse.execution()) {
					FirestoreService fs = getFirestore();
					if (fs != null) {
						ServerResponse saved = fs.saveWorker(worker);
						if (saved.execution())
							HistoryWorker.INSTANCE.add(worker);
						logger.info("Worker added successfully, total workers: {}",
								HistoryWorker.INSTANCE.getListWorker().size());
					}
					return ServerResponse.successfulCompletion("ADD");
				} else {
					return serverResponse;
				}
			} else if (result instanceof ValidationError(List<MessageExceptionValidation>errors,Object data)) {
				logger.warn("Validation failed: {} errors", errors.size());
				return ServerResponse.error("Incorrect data entered", errors, data);
			}
			logger.error("Unknown result type from builder");
			return ServerResponse.error("Internal server error while processing ADD command");
		} catch (Exception e) {
			logger.error("Error executing ADD command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
