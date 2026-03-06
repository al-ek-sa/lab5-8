package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class ShowCommand implements FindWorker {
    Logger logger = Logger.getLogger(ShowCommand.class.getName());

    public ShowCommand() {
    }

    /** The method outputs data of all registered employees. */
    public void execute(IOProvider io) {
        try {
            var list = HistoryWorker.getInstance().getListWorker();
            logger.log(Level.INFO, "Displaying collection");
            findWorker(logger);
            list.forEach(worker -> io.println(worker.toString()));
            logger.log(Level.INFO, "Collection displayed");
        } catch (Exception e) {
            logger.log(Level.INFO, "Displaying collection interrupted");
        }
    }
}
