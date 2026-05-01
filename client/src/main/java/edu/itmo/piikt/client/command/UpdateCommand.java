package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.commands.CommandExecute;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing Worker by ID.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommand implements CommandExecute<ServerResponse> {
	private static final AppLogger logger = new AppLogger(UpdateCommand.class);
	/** Network client for sending requests to server */
	private Network network;
	private AddCommand addCommand;

	/**
	 * Executes the UPDATE command
	 *
	 * @param io
	 *            input/output provider
	 * @return server response
	 */
	@Override
	public ServerResponse execute(IOProvider io, Object... arg) {
		try (Context ignored = Context.newId()) {
			if (arg.length != 2) {
				throw new RuntimeException();
			}
			if (!(arg[0] instanceof String command) || !(arg[1] instanceof String argument)) {
				throw new RuntimeException();
			}
			logger.info("UPDATE command started: id={}", argument);
			// Check if Worker exists
			ClientCommand clientCommand = ClientCommand.builder().nameCommand(command).argumentCommand(argument)
					.build();
			ServerResponse serverResponse = network.send(clientCommand);
			logger.debug("Initial response: success={}", serverResponse.execution());
			if (serverResponse.execution()) {
				// Worker exists - collect updated data
				logger.info("Fetching current data for update");
				return addCommand.execute(io);
			}
			// Worker not found or error
			logger.warn("Update failed: {}", serverResponse.message());
			return serverResponse;
		} catch (Exception e) {
			logger.error("UPDATE command failed: {}", e);
			throw new RuntimeException(e);
		}
	}
}
