package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.server.validation.modelValidation.ValidationWorker;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The class implements the command update id {element} : update the value of
 * the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class UpdateIdCommand {
    /**
     * The method replaces the element whose id is equal to the id specified by the
     * user.
     *
     * @throws RuntimeException
     *             If the user entered the id in an incorrect format.
     */
    public ServerResponse execute(ClientCommand clientCommand) {

        String id = clientCommand.getArgumentCommand();
        WorkerData workerData = (WorkerData) clientCommand.getData();

        if (id == null || id.trim().isEmpty()) {
            return ServerResponse.error("ID не введен");
        }

        var workers = HistoryWorker.INSTANCE.getListWorker();

        boolean match = workers.stream().anyMatch(worker -> worker.getUuid().equals(id));

        if (!match) {
            return ServerResponse.error("Нет работника с таким ID");
        }
        workers.removeIf(worker -> worker.getUuid().equals(argument));
        Worker newWorker = new ValidationWorker().worker(workerData);
        HistoryWorker.INSTANCE.add(newWorker);
    }
}
