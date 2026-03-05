package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class RemoveLowerCommander {
    Logger logger = Logger.getLogger(RemoveLowerCommander.class.getName());

    public RemoveLowerCommander() {
    }

    public void execute(IOProvider io, String argument) {
        try {
            HistoryWorker.getInstance(io).removeLower(argument);
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "Items deletion denied");
        }
    }
}
