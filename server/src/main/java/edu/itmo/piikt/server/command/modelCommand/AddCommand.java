package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.WorkerObject.BuilderWorker;
import edu.itmo.piikt.server.WorkerObject.ValidationError;
import edu.itmo.piikt.server.WorkerObject.WorkerBuilder;
import edu.itmo.piikt.server.commands.CommandType;
import edu.itmo.piikt.server.history.HistoryAddress;
import edu.itmo.piikt.server.history.HistoryCoordinate;
import edu.itmo.piikt.server.history.HistoryOrganization;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command add {element} : add a new element to the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class AddCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(AddCommand.class);
	private final BuilderWorker builderWorker = new BuilderWorker();
	private final WorkerBuilder workerBuilder = new WorkerBuilder();

	/**
	 * Executes the ADD command
	 *
	 * @param clientCommand
	 *            command containing WorkerData
	 * @return with success or error information
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			logger.info("Executing ADD command");
			WorkerData dataWorker = (WorkerData) clientCommand.getData();
			logger.debug("Worker data received: name={}, salary={}", dataWorker.getName(), dataWorker.getSalary());
			// Validate data
			Object result = builderWorker.data(dataWorker);
			if (result instanceof WorkerData) {
				Worker worker = workerBuilder.builerWorker(dataWorker);
				HistoryWorker.INSTANCE.add(worker);
				HistoryCoordinate.INSTANCE.add(worker.getCoordinates());
				HistoryOrganization.INSTANCE.add(worker.getOrganization());
				HistoryAddress.INSTANCE.add(worker.getOrganization().getOfficialAddress());
				logger.info("Worker added successfully, total workers: {}",
						HistoryWorker.INSTANCE.getListWorker().size());
				return ServerResponse.successfulCompletion("ADD");
			} else if (result instanceof ValidationError(java.util.List<edu.itmo.piikt.common.data.MessageExceptionValidation>errors,Object data)) {
				logger.warn("Validation failed: {} errors", errors.size());
				return ServerResponse.error("Incorrect data entered", errors, data);
			}
			logger.error("Unknown result type from builder");
			return ServerResponse.error("Internal server error while processing ADD command");
		} catch (Exception e) {
			logger.error("Error executing ADD command: {}", e);
			throw new RuntimeException(e);
		}
	}
}
