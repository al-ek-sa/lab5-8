package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.IOProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class RemoveByIdCommand implements IdMatches {
    Logger logger = Logger.getLogger(RemoveByIdCommand.class.getName());

    public RemoveByIdCommand() {
    }

    public void execute(IOProvider io, String argument) {
        try {
            logger.log(Level.INFO, "Deletion of item by ID started");
            idMatches(argument, logger);
            var listWorker = HistoryWorker.getInstance().getListWorker();
            try {
                listWorker.removeIf(worker -> worker.getId().equals(argument));
            } catch (RuntimeException e) {
                logger.log(Level.INFO, "Invalid input");
            }
        } catch (RuntimeException e) {
            logger.log(Level.INFO,
                    "Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
        }
    }
}
