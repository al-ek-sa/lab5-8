package edu.itmo.piikt.client.mode;

import edu.itmo.piikt.client.manager.CronValidationCommand;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Data
public class CronMode implements ClientMode {
	private static final AppLogger log = new AppLogger(CronMode.class);
	private final String command;

	@Override
	public String getName() {
		return NameMode.CRON.getName();
	}

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
