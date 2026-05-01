package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandSimple;
import lombok.NoArgsConstructor;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 5.2
 */
@NoArgsConstructor
public final class ExitCommand implements CommandSimple {
	private static final AppLogger logger = new AppLogger(ExitCommand.class);

	/**
	 * Executes the EXIT command
	 *
	 * @return ServerResponse with success message
	 */
	@Override
	public ServerResponse execute() {
		try (Context ignored = Context.newId()) {
			logger.info("Executing EXIT command, saving collection");
			logger.info("Collection saved, exiting");
			return ServerResponse.successfulCompletion("EXIT");
		} catch (Exception e) {
			logger.error("Error during exit: {}", e);
			throw new RuntimeException(e);
		}
	}
}
