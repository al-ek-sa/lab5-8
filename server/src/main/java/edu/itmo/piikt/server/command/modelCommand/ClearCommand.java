package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.commands.CommandSimple;
import edu.itmo.piikt.server.commands.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class ClearCommand implements CommandSimple {
	private static final AppLogger logger = new AppLogger(ClearCommand.class);

	/**
	 * Executes the CLEAR command
	 *
	 * @return ServerResponse with success message
	 */
	@Override
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			logger.info("Executing CLEAR command");
			int sizeBefore = HistoryWorker.INSTANCE.getListWorker().size();
			HistoryWorker.INSTANCE.clear();
			logger.info("Collection cleared. Workers removed: {}", sizeBefore);
			return ServerResponse.successfulCompletion(Commands.CLEAR.getName());
		} catch (Exception e) {
			logger.error("Error executing CLEAR command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
