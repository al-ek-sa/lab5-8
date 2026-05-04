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
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Command for updating an existing Worker by ID.
 * Performs remove_by_id followed by add operation.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class UpdateCommand implements CommandExecute<ServerResponse> {
	private static final AppLogger logger = new AppLogger(UpdateCommand.class);
	private final Network network;
	private final Worker worker;
	private String id;

	public UpdateCommand(Network network, Worker worker) {
		this.worker = worker;
		this.network = network;
	}

	/**
	 * Executes the UPDATE command.
	 * Removes existing worker by ID, then adds new version with same ID.
	 *
	 * @param io input/output provider for user interaction
	 * @param arg command arguments: [0] = worker ID, [1] = username
	 * @return server response with operation result
	 */
	@Override
	public ServerResponse execute(IOProvider io, Object... arg) {
		try (Context ignored = Context.newId()) {
			if (arg.length != 2) {
				logger.error("Invalid arguments count: expected 2, got {}", arg.length);
				throw new RuntimeException();
			}
			if (!(arg[1] instanceof String user) || !(arg[0] instanceof String argument)) {
				logger.error("Invalid argument types: user={}, argument={}", arg[1].getClass().getName(),
						arg[0].getClass().getName());
				throw new RuntimeException();
			}

			logger.info("UPDATE command started: targetId={}, user={}", argument, user);

			ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.REMOVE_BY_ID.getName())
					.user(user).argumentCommand(argument).build();

			ServerResponse serverResponse = network.send(clientCommand);
			logger.debug("REMOVE_BY_ID response: execution={}, message={}", serverResponse.execution(),
					serverResponse.message());

			if (serverResponse.execution()) {
				id = argument;
				logger.info("Worker with id={} removed successfully, proceeding with ADD", id);

				WorkerData workerData = worker.build(io);
				clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName()).user(user).data(workerData)
						.argumentCommand(id).build();

				serverResponse = network.send(clientCommand);
				logger.debug("ADD response: execution={}", serverResponse.execution());
				return add(serverResponse, io, user);
			}

			logger.warn("UPDATE failed: worker not found or remove failed: {}", serverResponse.message());
			return serverResponse;

		} catch (Exception e) {
			logger.error("UPDATE command failed: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Handles validation retries for ADD operation during update.
	 * Re-prompts user for corrected data if validation fails.
	 *
	 * @param serverResponse initial server response
	 * @param io input/output provider
	 * @param user username
	 * @return successful server response after correction
	 */
	private ServerResponse add(ServerResponse serverResponse, IOProvider io, String user) {
		var workerServer = new WorkerServer(io);
		var server = serverResponse;

		while (true) {
			try (Context ignored = Context.newId()) {
				if (server.execution()) {
					logger.info("UPDATE completed successfully");
					return server;
				} else {
					logger.warn("Validation error, requesting correction for user={}", user);
					var data = workerServer.build(server);
					ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName()).data(data)
							.argumentCommand(id).user(user).build();
					server = network.send(clientCommand);
					logger.debug("Retry ADD sent, waiting for response");
				}
			} catch (Exception e) {
				logger.error("Error in add retry: {}", e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
	}
}
