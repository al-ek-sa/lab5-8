package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command head : output the first element of the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class HeadCommand implements FindWorker {
    Logger logger = Logger.getLogger(HeadCommand.class.getName());

    public HeadCommand() {
    }
    /** The method outputs the data of the first element in the collection. */
    public void execute(IOProvider io) {
        var list = HistoryWorker.getInstance().getListWorker();
        logger.log(Level.INFO, "Displaying the last added element");
        findWorker(logger);
        io.println(list.getFirst().toString());
        logger.log(Level.INFO, "Element displayed on the screen");
    }
}
