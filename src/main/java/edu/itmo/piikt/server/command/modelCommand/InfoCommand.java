package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The class implements the command info : output information about the
 * collection to the standard output stream (type, initialization date, number
 * of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class InfoCommand {
    /** The method outputs data about the collection. */
    public ServerResponse doExecute() {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        var data = HistoryWorker.INSTANCE.getData();
        List<String> list = List.of("Collection type: " + listWorker.getClass().getSimpleName() + "\nIdentification time: " + data
                + "\nNumber of elements: " + listWorker.size());
        return ServerResponse.successfulCompletion("INFO: ", list);
    }
}
