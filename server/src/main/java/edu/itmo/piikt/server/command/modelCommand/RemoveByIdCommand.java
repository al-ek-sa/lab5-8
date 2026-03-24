package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(RemoveByIdCommand.class.getName());
    public ServerResponse execute(ClientCommand clientCommand) {
        String id = clientCommand.getArgumentCommand();
        if (id == null || id.trim().isEmpty()) {
            logger.info(LoggerCommand.REMOVE_BY_ID.getLogMessage());
            return ServerResponse.error("ID не введено");
        }
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        boolean match = listWorker.stream().anyMatch(worker -> worker.getUuid().equals(id));
        if (!match) {
            logger.info(LoggerCommand.REMOVE_BY_ID.getLogMessage());
            return ServerResponse.error("Работника с таким id не существует");
        }
        listWorker.removeIf(worker -> worker.getUuid().equals(id));
        logger.info(LoggerCommand.REMOVE_BY_ID.getLogMessage());
        return ServerResponse.successfulCompletion("REMOVE BY ID");
    }
}
