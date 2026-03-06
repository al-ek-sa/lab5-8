package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

import java.util.UUID;
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
        var listWorker = HistoryWorker.getInstance().getListWorker();
        try {
            UUID input = UUID.fromString(argument);
            logger.log(Level.INFO, "Deletion of items started");
            listWorker.removeIf(worker -> {
                UUID workerUuid = UUID.fromString(worker.getId());
                return workerUuid.compareTo(input) < 0;
            });
            logger.log(Level.INFO, "Items successfully deleted");
            // todo
        } catch (IllegalArgumentException e) {
            logger.log(Level.INFO, "Invalid UUID format");
        }
    }
}
