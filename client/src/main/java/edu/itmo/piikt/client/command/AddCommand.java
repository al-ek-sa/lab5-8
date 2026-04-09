package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.data.WorkerServer;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for adding a new Worker to the collection
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddCommand {
	private static final AppLogger logger = new AppLogger(AddCommand.class);
	/** Network client for sending requests to server */
	private Network network;
	/** Worker builder for collecting input data */
	private Worker worker = new Worker();

	/**
	 * Executes the ADD command.
	 *
	 * @param io
	 *            input/output provider for user interaction
	 * @return server response
	 */
	public ServerResponse execute(IOProvider io) {
		try (Context ignored = Context.newId()) {
			logger.info("ADD command started");
			// Build Worker from user input
			WorkerData workerData = worker.build(io);
			// Create and send command
			ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName()).data(workerData)
					.build();
			ServerResponse serverResponse = network.send(clientCommand);
			return add(serverResponse, io);
		} catch (Exception e) {
			logger.error("ADD command failed: {}", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Handles validation retries for ADD command
	 *
	 * @param serverResponse
	 *            initial server response
	 * @param io
	 *            input/output provider for user interaction
	 * @return successful server response
	 */
	private ServerResponse add(ServerResponse serverResponse, IOProvider io) {
		var workerServer = new WorkerServer(io);
		var server = serverResponse;
		while (true) {
			try (Context ignored = Context.newId()) {
				if (server.execution()) {
					logger.info("ADD command completed");
					return server;
				} else {
					logger.warn("Validation error, requesting correction");
					// Request corrected data from user
					var data = workerServer.build(server);
					ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName()).data(data)
							.build();
					server = network.send(clientCommand);
				}
			} catch (Exception e) {
				logger.error("Error in add retry: {}", e);
				throw new RuntimeException(e);
			}
		}
	}
}
