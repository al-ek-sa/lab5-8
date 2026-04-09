package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.NoArgsConstructor;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 5.2
 */
@NoArgsConstructor
public final class ExitCommand {
	private static final AppLogger logger = new AppLogger(ExitCommand.class);

	/**
	 * Executes the EXIT command
	 *
	 * @return ServerResponse with success message
	 */
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			logger.info("Executing EXIT command, saving collection");
			CSVParser csvParser = new CSVParser();
			csvParser.saveCollection();
			logger.info("Collection saved, exiting");
			return ServerResponse.successfulCompletion("EXIT");
		} catch (Exception e) {
			logger.error("Error during exit: {}", e);
			throw new RuntimeException(e);
		}
	}
}
