package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseArgumentCommand;
import edu.itmo.piikt.models.Worker;
import edu.itmo.piikt.validationModels.ValidationWorker;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command update id {element} : update the value of
 * the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class UpdateIdCommand implements IdMatches, BaseArgumentCommand {
    Logger logger = Logger.getLogger(UpdateIdCommand.class.getName());

    public UpdateIdCommand() {
    }
    /**
     * The method replaces the element whose id is equal to the id specified by the
     * user.
     *
     * @throws RuntimeException
     *             If the user entered the id in an incorrect format.
     */
    @Override
    public void doExecute(IOProvider io, String argument) {
        UUID.fromString(argument);
        idMatches(argument, logger);
        var workers = HistoryWorker.getInstance().getListWorker();
        workers.removeIf(w -> w.getId().equals(argument));
        Worker newWorker = new ValidationWorker().worker(io);
        HistoryWorker.getInstance().add(newWorker);
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Data successfully updated");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Invalid UUID format");
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Start of data update");
    }
}
