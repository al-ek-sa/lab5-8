package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.commands.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 4.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class RemoveLowerCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(RemoveLowerCommand.class);

	/**
	 * Executes the REMOVE_LOWER command
	 *
	 * @param clientCommand
	 *            command containing the date argument
	 * @return ServerResponse with success or error message
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("на данный момент, команда не доступна, повторите попытку позже");
			}
			String argument = clientCommand.getArgumentCommand();
			logger.info("Executing REMOVE_LOWER with argument: {}", argument);
			// Validate argument presence
			if (argument == null || argument.trim().isEmpty()) {
				logger.warn("Date argument is empty");
				return ServerResponse.error("Дата не введена");
			}

			LocalDate date;
			try {
				date = LocalDate.parse(argument.trim());
				logger.debug("Parsed date: {}", date);
			} catch (DateTimeParseException e) {
				logger.warn("Invalid date format: {}", argument);
				return ServerResponse.error("Неверный формат даты");
			}

			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			int sizeBefore = listWorker.size();
			listWorker.removeIf(worker -> worker.getStartDate().isAfter(date));
			int removed = sizeBefore - listWorker.size();
			logger.info("Removed {} workers with start date after {}", removed, date);
			return ServerResponse.successfulCompletion("REMOVE LOWER");
		} catch (Exception e) {
			logger.error("Error executing REMOVE_LOWER: {}", e);
			throw new RuntimeException(e);
		}
	}
}
