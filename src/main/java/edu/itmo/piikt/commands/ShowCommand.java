package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class ShowCommand implements FindWorker, BaseSimpleCommand {
    Logger logger = Logger.getLogger(ShowCommand.class.getName());

    public ShowCommand() {
    }

    /** The method outputs data of all registered employees. */
    @Override
    public void doExecute(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        findWorker(logger);
        list.forEach(worker -> io.println(worker.toString()));
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Displaying collection");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Displaying collection interrupted");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Collection displayed");
    }
}
