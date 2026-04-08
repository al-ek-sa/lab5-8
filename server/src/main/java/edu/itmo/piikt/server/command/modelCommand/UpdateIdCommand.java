package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command update id {element} : update the value of
 * the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class UpdateIdCommand {
	private static final AppLogger logger = new AppLogger(UpdateIdCommand.class);

	/**
	 * The method replaces the element whose id is equal to the id specified by the
	 * user.
	 */
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			String id = clientCommand.getArgumentCommand();
			logger.info("Executing UPDATE command for id: {}", id);
			if (id == null || id.trim().isEmpty()) {
				logger.warn("ID is empty");
				return ServerResponse.error("ID не введен");
			}
			var workers = HistoryWorker.INSTANCE.getListWorker();
			boolean match = workers.stream().anyMatch(worker -> worker.getUuid().equals(id));
			if (!match) {
				logger.warn("Worker with id {} not found", id);
				return ServerResponse.error("Нет работника с таким ID");
			}
			workers.removeIf(worker -> worker.getUuid().equals(id));
			logger.info("Worker with id {} removed, ready for update", id);
			return ServerResponse.successfulCompletion("Работник найден");
		} catch (Exception e) {
			logger.error("Error executing UPDATE command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
