package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command head : output the first element of the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class HeadCommand {
    /** The method outputs the data of the first element in the collection. */
    public ServerResponse execute() {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        if(listWorker.isEmpty()) {
            return ServerResponse.successfulCompletion("COLLECTION IS EMPTY");
        }
        String input = listWorker.getFirst().toString();
        return ServerResponse.successfulCompletion("HEAD WORKER", input);
    }
}
