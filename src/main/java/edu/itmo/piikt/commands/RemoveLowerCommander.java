package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseArgumentCommand;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class RemoveLowerCommander implements BaseArgumentCommand {
    Logger logger = Logger.getLogger(RemoveLowerCommander.class.getName());

    public RemoveLowerCommander() {
    }
    @Override
    public void doExecute(IOProvider io, String argument) {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        UUID input = UUID.fromString(argument);
        listWorker.removeIf(worker -> {
            UUID workerUuid = UUID.fromString(worker.getId());
            return workerUuid.compareTo(input) < 0;
        });
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Items successfully deleted");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Invalid UUID format");
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Deletion of items started");
    }
}
