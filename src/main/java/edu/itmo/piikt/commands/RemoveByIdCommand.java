package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseArgumentCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class RemoveByIdCommand implements IdMatches, BaseArgumentCommand {
    Logger logger = Logger.getLogger(RemoveByIdCommand.class.getName());

    public RemoveByIdCommand() {
    }
    @Override
    public void doExecute(IOProvider io, String argument) {
        idMatches(argument, logger);
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        listWorker.removeIf(worker -> worker.getId().equals(argument));
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Deletion of item by ID started");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE,
                "Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
    }

}
