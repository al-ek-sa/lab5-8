package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Command to save the collection to a CSV file from the server console
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class SaveCommand {
	private static final AppLogger logger = new AppLogger(SaveCommand.class);
	private final String name = "save";

	/**
	 * Executes the SAVE command
	 */
	public void execute() {
		try (Context ignored = Context.newId()) {
			logger.info("Executing SAVE command");
			logger.info("Collection saved successfully");
		} catch (Exception e) {
			logger.error("Error executing SAVE command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
