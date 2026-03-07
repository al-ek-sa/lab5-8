package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.Confirmation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class ClearCommand implements Confirmation, BaseSimpleCommand {
    Logger logger = Logger.getLogger(ClearCommand.class.getName());
    public ClearCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        Boolean consent = confirmation(io);
        if (consent == true) {
            HistoryWorker.INSTANCE.clear();

        }
    }

    @Override
    public void question(IOProvider io) {
        io.printlnCommand("Are you sure you want to clear the collection? (yes/no)");
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Consent received, clearing collection");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Collection cleared successfully");
    }

    @Override
    public void refusal() {
        logger.log(Level.INFO, "Consent received, clearing collection");
    }
}
