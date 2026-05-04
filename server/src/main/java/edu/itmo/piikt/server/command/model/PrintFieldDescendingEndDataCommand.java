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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command print_field_descending_end_date : output the
 * endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class PrintFieldDescendingEndDataCommand implements CommandSimple {
	private static final AppLogger logger = new AppLogger(PrintFieldDescendingEndDataCommand.class);

	/**
	 * Executes the PRINT_FIELD_DESCENDING_END_DATE command
	 *
	 * @return ServerResponse with sorted worker list
	 */
	@Override
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("         return ServerResponse.error(\"Command unavailable, please try again later");
			}
			logger.info("Executing PRINT_FIELD_DESCENDING_END_DATE command");
			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			if (listWorker.isEmpty()) {
				logger.debug("Collection is empty");
				return ServerResponse.successfulCompletion("COLLECTION IS EMPTY");
			}
			var sortedList = new LinkedList<>(listWorker);
			List<String> list = sortedList.stream()
					.sorted(Comparator.comparing(Worker::getEndDate, Comparator.nullsFirst(Comparator.naturalOrder()))
							.reversed().thenComparing(Comparator.naturalOrder()))
					.map(Worker::toString).collect(Collectors.toList());
			logger.debug("Sorted {} workers by end date", list.size());
			return ServerResponse.successfulCompletion("END DATE: ", list);
		} catch (Exception e) {
			logger.error("Error executing PRINT_FIELD_DESCENDING_END_DATE: {}", e);
			throw new RuntimeException(e);
		}
	}
}
