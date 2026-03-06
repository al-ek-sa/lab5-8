package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command filter_contains_name name : output elements
 * whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class FilterContainsNameCommand {
    Logger logger = Logger.getLogger(FilterContainsNameCommand.class.getName());

    public FilterContainsNameCommand() {
    }
    /**
     * The method outputs all employees with the same name as entered by the user.
     *
     * @param argument
     *            The name entered by the user.
     */
    public void execute(IOProvider io, String argument) {
        try {
            logger.log(Level.INFO, "Search users by name");
            var list = HistoryWorker.getInstance().getListWorker();
            list.stream().filter(worker -> worker.getName() != null).filter(worker -> worker.getName().equals(argument))
                    .forEach(worker -> io.println(worker.toString()));
            logger.log(Level.INFO, "All users with the entered name have been displayed");
        } catch (Exception e) {
            logger.log(Level.INFO, "Search failed");
        }
    }
}
