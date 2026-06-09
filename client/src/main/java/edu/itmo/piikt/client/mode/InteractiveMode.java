package edu.itmo.piikt.client.mode;

import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;

/**
 * Interactive mode for manual command input
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class InteractiveMode implements ClientMode {

	private static final AppLogger logger = new AppLogger(InteractiveMode.class);

	/**
	 * Executes the interactive mode
	 *
	 * @param existingNetwork
	 *            network client instance
	 * @param inputProvider
	 *            input/output provider for console
	 */
	@Override
	public void execute(Network existingNetwork, IOProvider inputProvider) {
		try (Context ignored = Context.newId()) {
			logger.info("Starting interactive mode");
			try {
				ValidationCommand.INSTANCE.setNetwork(existingNetwork);
				ValidationCommand.INSTANCE.validation(inputProvider);
			} catch (Exception e) {
				logger.error("Error in command processing: {}", e.getMessage());
			}
		}
	}
}
