package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;

import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command print_field_descending_end_date : output the
 * endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class PrintFieldDescendingEndDataCommand implements FindWorker, BaseSimpleCommand {
    Logger logger = Logger.getLogger(PrintFieldDescendingEndDataCommand.class.getName());

    public PrintFieldDescendingEndDataCommand() {
    }

    /**
     * The method sorts employees by endDate; if the data matches, employees are
     * sorted by id.
     */
    @Override
    public void doExecute(IOProvider io) {
        findWorker(logger);
        var listWorker = HistoryWorker.getInstance().getListWorker();
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
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Failed to sort");
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Sorting by date of dismissal started");
    }
}
