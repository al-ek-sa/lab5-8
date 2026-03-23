package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class RemoveByIdCommand {
    public ServerResponse execute(ClientCommand clientCommand) {
        String id = clientCommand.getArgumentCommand();

        if (id == null || id.trim().isEmpty()) {
            return ServerResponse.error("ID не введено");
        }

        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        boolean match = listWorker.stream().anyMatch(worker -> worker.getUuid().equals(id));
        if (!match) {
            return ServerResponse.error("Работника с таким id не существует");
        }
        listWorker.removeIf(worker -> worker.getUuid().equals(id));
        return ServerResponse.successfulCompletion("REMOVE BY ID");
    }
}
