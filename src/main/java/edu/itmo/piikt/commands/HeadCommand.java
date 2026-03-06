package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command head : output the first element of the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class HeadCommand implements FindWorker, BaseSimpleCommand {
    Logger logger = Logger.getLogger(HeadCommand.class.getName());

    public HeadCommand() {
    }
    /** The method outputs the data of the first element in the collection. */
    @Override
    public void doExecute(IOProvider io) {
        var list = HistoryWorker.getInstance().getListWorker();
        findWorker(logger);
        io.println(list.getFirst().toString());
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Displaying the last added element");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Element displayed on the screen");
    }
}
