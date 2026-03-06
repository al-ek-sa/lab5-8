package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.validationModels.ValidationWorker;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command add {element} : add a new element to the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

// public class AddCommand implements Command {
public final class AddCommand implements BaseSimpleCommand {
    Logger logger = Logger.getLogger(AddCommand.class.getName());

    public AddCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        ValidationWorker worker = new ValidationWorker();
        HistoryWorker.getInstance().add(worker.worker(io));
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Item successfully added");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Start adding an item");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, e.getMessage());
    }
}
