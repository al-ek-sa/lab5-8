package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(UpdateIdCommand.class.getName());
    /**
     * The method replaces the element whose id is equal to the id specified by the
     * user.
     */
    // todo дароботать на клиенте если успешное выполнение то будет добавление
    public ServerResponse execute(ClientCommand clientCommand) {
        String id = clientCommand.getArgumentCommand();
        if (id == null || id.trim().isEmpty()) {
            logger.info(LoggerCommand.UPDATE.getLogMessage());
            return ServerResponse.error("ID не введен");
        }
        var workers = HistoryWorker.INSTANCE.getListWorker();
        boolean match = workers.stream().anyMatch(worker -> worker.getUuid().equals(id));
        if (!match) {
            logger.info(LoggerCommand.UPDATE.getLogMessage());
            return ServerResponse.error("Нет работника с таким ID");
        }
        workers.removeIf(worker -> worker.getUuid().equals(id));
        logger.info(LoggerCommand.UPDATE.getLogMessage());
        return ServerResponse.successfulCompletion("Работник найден");
    }
}
