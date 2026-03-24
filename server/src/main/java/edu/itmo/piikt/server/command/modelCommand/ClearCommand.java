package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.util.logging.Logger;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class ClearCommand {
    private static final Logger logger = Logger.getLogger(ClearCommand.class.getName());
    public ServerResponse execute() {
        HistoryWorker.INSTANCE.clear();
        logger.info(LoggerCommand.CLEAR.getLogMessage());
        return ServerResponse.successfulCompletion(Commands.CLEAR.getName());
    }
}
