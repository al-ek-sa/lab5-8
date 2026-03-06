package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.IOProvider;
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
public class UpdateIdCommand implements IdMatches {
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
    public void execute(IOProvider io, String argument) {
        try {
            UUID.fromString(argument);

            idMatches(argument, logger);

            logger.log(Level.INFO, "Start of data update");

            var workers = HistoryWorker.getInstance().getListWorker();
            workers.removeIf(w -> w.getId().equals(argument));

            Worker newWorker = new ValidationWorker().worker(io);
            HistoryWorker.getInstance().add(newWorker);

            logger.log(Level.INFO, "Data successfully updated");

        } catch (IllegalArgumentException e) {
            logger.log(Level.INFO, "Invalid UUID format");
        }
    }
}
