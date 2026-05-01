package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandSimple;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The class implements the command info : output information about the
 * collection to the standard output stream (type, initialization date, number
 * of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class InfoCommand implements CommandSimple {
	private static final AppLogger logger = new AppLogger(InfoCommand.class);

	/**
	 * Executes the INFO command
	 *
	 * @return ServerResponse with collection information
	 */
	@Override
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			logger.info("Executing INFO command");
			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			var data = HistoryWorker.INSTANCE.getData();
			List<String> list = List.of("Collection type: " + listWorker.getClass().getSimpleName()
					+ "\nIdentification time: " + data + "\nNumber of elements: " + listWorker.size());
			logger.debug("Collection info: type={}, size={}, created={}", listWorker.getClass().getSimpleName(),
					listWorker.size(), data);
			ServerResponse.successfulCompletion("INFO: ", list);
			return ServerResponse.successfulCompletion("INFO: ", list);
		} catch (Exception e) {
			logger.error("Error executing INFO command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
