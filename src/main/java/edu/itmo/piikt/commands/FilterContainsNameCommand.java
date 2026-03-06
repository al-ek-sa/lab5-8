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
 * @version 1.0
 */
public class FilterContainsNameCommand {
    Logger logger = Logger.getLogger(FilterContainsNameCommand.class.getName());

    public FilterContainsNameCommand() {
    }

    public void execute(IOProvider io, String argument) {
        try {
            logger.log(Level.INFO, "Search users by name");
            HistoryWorker.getInstance().printName(argument, io);
        } catch (Exception e) {
            logger.log(Level.INFO, "Search failed");
        }
    }
}
