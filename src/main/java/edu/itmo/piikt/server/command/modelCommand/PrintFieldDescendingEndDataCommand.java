package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.interfaces.FindWorker;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
import edu.itmo.piikt.common.models.Worker;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * The class implements the command print_field_descending_end_date : output the
 * endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see IOProvider
 * @see HistoryWorker
 * @see FindWorker
 * @see BaseSimpleCommand
 */
@NoArgsConstructor
public final class PrintFieldDescendingEndDataCommand implements FindWorker, BaseSimpleCommand {
    /**
     * The method sorts employees by endDate; if the data matches, employees are
     * sorted by id.
     */
    // todo через map получить строковое значение
    @Override
    public void doExecute(IOProvider io) {
        if (findWorker(io)) {
            return;
        }
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        var sortedList = new LinkedList<>(listWorker);
        sortedList.stream()
                .sorted(Comparator.comparing(Worker::getEndDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .reversed().thenComparing(Comparator.naturalOrder()))
                .forEach(worker -> io.println(worker.toString()));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.PRINT_FIELD_DESCENDING_END_DATE;
    }
}
