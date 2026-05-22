package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.bd.SearchWorker;
import edu.itmo.piikt.server.command.bd.WorkerAdd;
import edu.itmo.piikt.server.command.bd.WorkerFlag;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.FirestoreService;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 4.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class RemoveByIdCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(RemoveByIdCommand.class);
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
	 * Executes the REMOVE_BY_ID command
	 *
	 * @param clientCommand
	 *            command containing the worker ID
	 * @return ServerResponse with success or error message
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse
						.error("         return ServerResponse.error(\"Command unavailable, please try again later");
			}
			String id = clientCommand.argumentCommand();
			logger.info("Executing REMOVE_BY_ID with id: {}", id);

			if (id == null || id.trim().isEmpty()) {
				logger.warn("ID is empty");
				return ServerResponse.error("ID не введено");
			}
			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			boolean match = listWorker.stream().anyMatch(worker -> worker.getUuid().equals(id));
			if (!match) {
				logger.warn("Worker with id {} not found", id);
				return ServerResponse.error("Работника с таким id не существует");
			}
			ServerResponse serverResponse1 = WorkerFlag.getWorkerIdsByUserId(clientCommand, id);
			ServerResponse serverResponse = SearchWorker.getWorkerIdsByUserId(clientCommand, id);

			if (!serverResponse.execution() && serverResponse1.execution()) {
				return serverResponse;
			}
			serverResponse = getFirestore().deleteWorker(id);
			if (serverResponse.exception()) {
				return serverResponse;
			}
			listWorker.removeIf(worker -> worker.getUuid().equals(id));
			logger.info("Worker with id {} removed successfully", id);
			return ServerResponse.successfulCompletion("REMOVE BY ID");
		} catch (Exception e) {
			logger.error("Error executing REMOVE_BY_ID: {}", e);
			throw new RuntimeException(e);
		}
	}
}
