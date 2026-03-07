package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseArgumentCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command filter_contains_name name : output elements
 * whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class FilterContainsNameCommand implements BaseArgumentCommand {
    Logger logger = Logger.getLogger(FilterContainsNameCommand.class.getName());

    public FilterContainsNameCommand() {
    }
    /**
     * The method outputs all employees with the same name as entered by the user.
     *
     * @param argument
     *            The name entered by the user.
     */
    @Override
    public void doExecute(IOProvider io, String argument) {

        var list = HistoryWorker.INSTANCE.getListWorker();
        list.stream().filter(worker -> worker.getName() != null).filter(worker -> worker.getName().equals(argument))
                .forEach(worker -> io.println(worker.toString()));
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Search users by name");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "All users with the entered name have been displayed");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Search failed");
    }
}
