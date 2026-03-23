package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
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
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class AddCommand {
    private final BuilderWorker builderWorker = new BuilderWorker();
    private final WorkerBuilder workerBuilder = new WorkerBuilder();

    public ServerResponse execute(ClientCommand clientCommand) {
        WorkerData dataWorker = (WorkerData) clientCommand.getData();
        Object result = builderWorker.data(dataWorker);
        if (result instanceof WorkerData) {
            Worker worker = workerBuilder.builerWorker(dataWorker);
            HistoryWorker.INSTANCE.add(worker);
            return ServerResponse.successfulCompletion("ADD");
        } else if (result instanceof ValidationError) {
            ValidationError error = (ValidationError) result;
            return ServerResponse.errer("Введены неверные данные", error.getErrors(), error.getData());
        }
        return ServerResponse.error("Какая-то ошибка");
    }
}
