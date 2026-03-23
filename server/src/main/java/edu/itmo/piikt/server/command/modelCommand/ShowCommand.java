package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Worker;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class ShowCommand {
    /** The method outputs data of all registered employees. */
    public ServerResponse execute() {
        var listHistory = HistoryWorker.INSTANCE.getListWorker();
        if (listHistory.isEmpty()) {
            return ServerResponse.error("COLLECTION IS EMPTY");
        }
        List<String> list = listHistory.stream().sorted(Comparator.comparing(Worker::getName)
                .thenComparing(Worker::getStartDate).thenComparing(Worker::getCreationDate)).map(Worker::toString)
                .collect(Collectors.toList());
        return ServerResponse.successfulCompletion("SHOW: ", list);
    }
}
