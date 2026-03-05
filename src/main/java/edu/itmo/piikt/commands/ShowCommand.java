package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ShowCommand {
    Logger logger = Logger.getLogger(ShowCommand.class.getName());

    public ShowCommand() {
    }

    public void execute(IOProvider io) {
        try {
            logger.log(Level.INFO, "Displaying collection");
            HistoryWorker.getInstance(io).printHistoryWorker();
            logger.log(Level.INFO, "Collection displayed");
        } catch (Exception e) {
            logger.log(Level.INFO, "Displaying collection interrupted");
        }
    }
}
