package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.manager.BDConnect;
import edu.itmo.piikt.server.manager.FirestoreService;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
	private FirestoreService firestore;
	private FirestoreService getFirestore() {
		if (firestore == null) {
			try {
				firestore = new FirestoreService();
			} catch (IOException e) {
				logger.error("Failed to initialize Firestore: {}", e.getMessage(), e);
			}
		}
		return firestore;
	}
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
			// проверить права

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
			List<Worker> listRemove = listWorker.stream().filter(worker -> worker.getStartDate().isAfter(date))
					.toList();
			ServerResponse serverResponse = null;
			for (Worker worker : listRemove)
				serverResponse = getFirestore().deleteWorker(worker.getUuid());
			assert serverResponse != null;
			if (!serverResponse.exception()) {
				return serverResponse;
			}
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
