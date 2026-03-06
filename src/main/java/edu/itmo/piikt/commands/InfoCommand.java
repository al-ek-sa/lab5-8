package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command info : output information about the
 * collection to the standard output stream (type, initialization date, number
 * of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class InfoCommand {
    Logger logger = Logger.getLogger(InfoCommand.class.getName());

    public InfoCommand() {
    }

    public void execute(IOProvider io) {
        try {
            logger.log(Level.INFO, "Displaying information about the collection");
            HistoryWorker.getInstance().infoLiat(io);
            logger.log(Level.INFO, "Information successfully displayed");
        } catch (Exception e) {
            io.printException("Information not displayed");
        }
    }
}
