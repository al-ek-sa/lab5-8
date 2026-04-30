package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.commands.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command filter_contains_name name : output elements
 * whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class FilterContainsNameCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(FilterContainsNameCommand.class);

	/**
	 * Executes the FILTER_CONTAINS_NAME command
	 *
	 * @param clientCommand
	 *            command containing the name substring
	 * @return ServerResponse with filtered worker list
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("на данный момент, команда не доступна, повторите попытку позже");
			}
			String argument = clientCommand.getArgumentCommand();
			logger.info("Executing FILTER_CONTAINS_NAME with argument: {}", argument);
			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			List<String> list = listWorker.stream().filter(worker -> worker.getName() != null)
					.filter(worker -> worker.getName().contains(argument)).map(Worker::toString)
					.collect(Collectors.toList());
			logger.debug("Found {} workers containing '{}'", list.size(), argument);
			return ServerResponse.successfulCompletion("FILTER NAME", list);
		} catch (Exception e) {
			logger.error("Error executing FILTER_CONTAINS_NAME: {}", e);
			throw new RuntimeException(e);
		}
	}
}
