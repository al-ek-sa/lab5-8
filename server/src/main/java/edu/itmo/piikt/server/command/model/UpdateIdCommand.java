package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
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
public final class UpdateIdCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(UpdateIdCommand.class);

	/**
	 * Executes the UPDATE command
	 *
	 * @param clientCommand
	 *            command containing the worker ID
	 * @return ServerResponse indicating whether worker exists
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("на данный момент, команда не доступна, повторите попытку позже");
			}
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
