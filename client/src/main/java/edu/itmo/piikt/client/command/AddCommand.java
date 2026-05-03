package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.commands.CommandExecute;
import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.data.WorkerServer;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

/**
 * Command for adding a new Worker to the collection
 *
 * @param network
 *            Network client for sending requests to server
 * @param worker
 *            Worker builder for collecting input data
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record AddCommand(Network network, Worker worker) implements CommandExecute<ServerResponse> {
	private static final AppLogger logger = new AppLogger(AddCommand.class);

	/**
	 * Executes the ADD command.
	 *
	 * @param io
	 *            input/output provider for user interaction
	 * @return server response
	 */
	@Override
	public ServerResponse execute(IOProvider io, Object... arg) {
		try (Context ignored = Context.newId()) {
			if (arg.length > 1) {
				throw new RuntimeException();
			}
			logger.info("ADD command started");
			// Build Worker from user input
			WorkerData workerData = worker.build(io);
			// Create and send command
			ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName())
					.user((String) arg[0]).data(workerData).build();
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
