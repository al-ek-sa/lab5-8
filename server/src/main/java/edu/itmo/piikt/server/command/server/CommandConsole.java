package edu.itmo.piikt.server.command.server;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Console command handler for the server-side. Provides an interactive console
 * loop for executing server commands.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@NoArgsConstructor
@Getter
public enum CommandConsole {
	INSTANCE;

	private static final AppLogger logger = new AppLogger(CommandConsole.class);
	private final CommandFactory commandFactory = new CommandFactory();

	/**
	 * Starts the console command execution loop. Continuously reads commands from
	 * input and processes them. The loop runs indefinitely until the application is
	 * terminated.
	 *
	 * @param io
	 *            input/output provider for reading console commands
	 */
	public void execute(IOProvider io) {
		while (true) {
			String command = io.readLine().toLowerCase();
			logger.debug("Console input received : '{}'", command);

			try {
				if (command.equals("exit")) {
					logger.info("Shutting down server console");
					break;
				}
				commandFactory.execute(command, io);
				logger.debug("Command '{}' executed successfully", command);
			} catch (Exception e) {
				logger.error("Failed to execute console command '{}': {}", command, e.getMessage(), e);
				io.println("Error executing command: " + e.getMessage());
			}
		}
	}
}
