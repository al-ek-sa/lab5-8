package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandSimple;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class ShowCommand implements CommandSimple {
	private static final AppLogger logger = new AppLogger(ShowCommand.class);

	/**
	 * Executes the SHOW command
	 *
	 * @return ServerResponse with sorted worker list or error if collection is
	 *         empty
	 */
	@Override
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("         return ServerResponse.error(\"Command unavailable, please try again later");
			}
			logger.info("Executing SHOW command");
			var listHistory = HistoryWorker.INSTANCE.getListWorker();
			if (listHistory.isEmpty()) {
				logger.debug("Collection is empty");
				return ServerResponse.error("COLLECTION IS EMPTY");
			}
			// Sort workers: by name, then start date
			List<String> list = listHistory.stream()
					.sorted(Comparator.comparing(Worker::getName).thenComparing(Worker::getStartDate))
					.map(Worker::toString).collect(Collectors.toList());
			logger.debug("Showing {} workers", list.size());
			return ServerResponse.successfulCompletion("SHOW: ", list);
		} catch (Exception e) {
			logger.error("Error executing SHOW command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
