package edu.itmo.piikt.client.mode;

import edu.itmo.piikt.client.manager.CronValidationCommand;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;

/**
 * Cron mode for executing a single command
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record CronMode(String command) implements ClientMode {
	private static final AppLogger log = new AppLogger(CronMode.class);

	/**
	 * Executes the cron mode
	 *
	 * @param network
	 *            network client instance
	 * @param io
	 *            input/output provider for console
	 */
	@Override
	public void execute(Network network, IOProvider io) {
		try (Context ignored = Context.newId()) {
			log.info("Cron mode: executing command '{}'", command);
			CronValidationCommand.INSTANCE.setNetwork(network);
			CronValidationCommand.INSTANCE.validation(io, command);
			log.info("Cron mode completed");
		} catch (Exception e) {
			log.error("Cron mode failed: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
