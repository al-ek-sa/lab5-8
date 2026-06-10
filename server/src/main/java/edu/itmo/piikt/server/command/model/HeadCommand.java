package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandSimple;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The class implements the command head : output the first element of the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class HeadCommand implements CommandSimple {
	private static final AppLogger logger = new AppLogger(HeadCommand.class);

	/**
	 * Executes the HEAD command
	 *
	 * @return ServerResponse with the first worker or empty message
	 */
	@Override
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("Service temporarily unavailable, please try again later");
			}
			logger.info("Executing HEAD command");
			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			if (listWorker.isEmpty()) {
				logger.debug("Collection is empty");
				return ServerResponse.successfulCompletion("Коллекция пуста");
			}
			String input = listWorker.getFirst().toString();
			logger.debug("First worker: {}", input);
			return ServerResponse.successfulCompletion(input);
		} catch (Exception e) {
			logger.error("Error executing HEAD command: {}", e);
			return ServerResponse.error("Error getting first worker: " + e.getMessage());
		}
	}
}
