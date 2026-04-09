package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class RemoveByIdCommand {
	private static final AppLogger logger = new AppLogger(RemoveByIdCommand.class);

	/**
	 * Executes the REMOVE_BY_ID command
	 * @param clientCommand command containing the worker ID
	 * @return ServerResponse with success or error message
	 */
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			String id = clientCommand.getArgumentCommand();
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

			listWorker.removeIf(worker -> worker.getUuid().equals(id));
			logger.info("Worker with id {} removed successfully", id);
			return ServerResponse.successfulCompletion("REMOVE BY ID");
		} catch (Exception e) {
			logger.error("Error executing REMOVE_BY_ID: {}", e);
			throw new RuntimeException(e);
		}
	}
}
