package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command info : output information about the
 * collection to the standard output stream (type, initialization date, number
 * of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class InfoCommand implements BaseSimpleCommand {
    Logger logger = Logger.getLogger(InfoCommand.class.getName());

    public InfoCommand() {
    }
    @Override
    /** The method outputs data about the collection. */
    public void doExecute(IOProvider io) {
        var listWorker = HistoryWorker.getInstance().getListWorker();
        var data = HistoryWorker.getInstance().getData();
        io.println("Collection type: " + listWorker.getClass() + "\nIdentification time: " + data
                + "\nNumber of elements: " + listWorker.size());

    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Displaying information about the collection");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Information successfully displayed");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Information not displayed");
    }
}
