package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class ClearCommand {
    public ServerResponse execute() {
        HistoryWorker.INSTANCE.clear();
        return ServerResponse.successfulCompletion("CLEAR");
    }
}
