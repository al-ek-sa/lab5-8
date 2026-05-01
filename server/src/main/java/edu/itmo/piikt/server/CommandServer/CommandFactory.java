package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;

/**
 * Factory for processing console commands on the server side
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class CommandFactory {
	private static final AppLogger logger = new AppLogger(CommandFactory.class);
	private final SaveCommand saveCommand = new SaveCommand();

	/**
	 * Executes a console command
	 *
	 * @param command
	 *            command string entered in console
	 */
	public void execute(String command, IOProvider io) {
		try (Context ignored = Context.newId()) {
			logger.debug("Processing console command: {}", command);
			// Check for "save" command with typo correction
			if (DamerauLevenshteinDistance.distance(command, saveCommand.getName()) <= 1) {
				logger.info("Executing SAVE command from console");
				saveCommand.execute(io);
			} else {
				logger.debug("Unknown console command: {}", command);
			}
		} catch (Exception e) {
			logger.error("Error executing console command: {}", e);
		}
	}
}
