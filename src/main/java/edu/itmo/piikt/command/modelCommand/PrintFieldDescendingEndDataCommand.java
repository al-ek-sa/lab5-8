package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;

import java.util.Collections;
import java.util.LinkedList;

/**
 * The class implements the command print_field_descending_end_date : output the
 * endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see IOProvider
 * @see HistoryWorker
 * @see FindWorker
 * @see BaseSimpleCommand
 */
public final class PrintFieldDescendingEndDataCommand implements FindWorker, BaseSimpleCommand {
    public PrintFieldDescendingEndDataCommand() {
    }

    /**
     * The method sorts employees by endDate; if the data matches, employees are
     * sorted by id.
     */
    @Override
    public void doExecute(IOProvider io) {
        if (findWorker(io)) {
            return;
        }
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        var sortedList = new LinkedList<>(listWorker);
        sortedList.sort((worker1, worker2) -> {
            if (worker1.getEndDate() == null && worker2.getEndDate() == null) {
                return worker1.getId().compareTo(worker2.getId());
            }
            if (worker1.getEndDate() == null)
                return 1;
            if (worker2.getEndDate() == null)
                return -1;
            return worker1.getEndDate().compareTo(worker2.getEndDate());
        });
        Collections.reverse(sortedList);
        sortedList.forEach(worker -> io.println(worker.toString()));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.PRINT_FIELD_DESCENDING_END_DATE;
    }
}
