package edu.itmo.piikt.server.command.modelCommand;

import ch.qos.logback.classic.Logger;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.WorkerObject.BuilderWorker;
import edu.itmo.piikt.server.WorkerObject.ValidationError;
import edu.itmo.piikt.server.WorkerObject.WorkerBuilder;
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
public final class AddCommand {
    private static final AppLogger logger = new AppLogger(AddCommand.class);
    private final BuilderWorker builderWorker = new BuilderWorker();
    private final WorkerBuilder workerBuilder = new WorkerBuilder();

    public ServerResponse execute(ClientCommand clientCommand) {
        try (Context context = Context.newId()) {
            logger.info("Executing ADD command");
            WorkerData dataWorker = (WorkerData) clientCommand.getData();
            logger.debug("Worker data received: name={}, salary={}", dataWorker.getName(), dataWorker.getSalary());
            Object result = builderWorker.data(dataWorker);
            if (result instanceof WorkerData) {
                Worker worker = workerBuilder.builerWorker(dataWorker);
                HistoryWorker.INSTANCE.add(worker);
                logger.info("Worker added successfully, total workers: {}", HistoryWorker.INSTANCE.getListWorker().size());
                return ServerResponse.successfulCompletion("ADD");
            } else if (result instanceof ValidationError) {
                ValidationError error = (ValidationError) result;
                logger.warn("Validation failed: {} errors", error.getErrors().size());
                return ServerResponse.error("Введены неверные данные", error.getErrors(), error.getData());
            }
            logger.error("Unknown result type from builder");
            return ServerResponse.error("Какая-то ошибка");
        } catch (Exception e) {
            logger.error("Error executing ADD command: {}", e);
            throw new RuntimeException(e);
        }
    }
}