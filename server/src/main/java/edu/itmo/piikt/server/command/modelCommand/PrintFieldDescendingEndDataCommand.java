package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Worker;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command print_field_descending_end_date : output the
 * endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class PrintFieldDescendingEndDataCommand {
    /**
     * The method sorts employees by endDate; if the data matches, employees are
     * sorted by id.
     */
    public ServerResponse execute() {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        if (listWorker.isEmpty()) {
            return ServerResponse.successfulCompletion("COLLECTION IS EMPTY");
        }
        var sortedList = new LinkedList<>(listWorker);
        List<String> list = sortedList.stream()
                .sorted(Comparator.comparing(Worker::getEndDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .reversed().thenComparing(Comparator.naturalOrder()))
                .map(Worker::toString).collect(Collectors.toList());
        return ServerResponse.successfulCompletion("END DATE: ", list);
    }
}
