package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Confirmation;
import edu.itmo.piikt.validationModels.GeneratorId;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ClearCommand implements Confirmation {
    Logger logger = Logger.getLogger(ClearCommand.class.getName());

    public ClearCommand() {
    }

    public void execute(IOProvider io) {
        if (io.name().equals("Console")) {
            io.printlnCommand("Are you sure you want to clear the collection? (yes/no)");
            Boolean consent = confirmation(io);
            if (consent == true) {
                logger.log(Level.INFO, "Consent received, clearing collection");
                HistoryWorker.getInstance(io).clear();
                GeneratorId.getInstance(io).setStartId(1);
                logger.log(Level.INFO, "Collection cleared successfully");
            } else {
                logger.log(Level.INFO, "Consent received, clearing collection");
                GeneratorId.getInstance(io).setStartId(1);
            }
        }

        if (io.name().equals("File")) {
            io.printlnCommand("Consent received, clearing collection");
            HistoryWorker.getInstance(io).clear();
            GeneratorId.getInstance(io).setStartId(1);
            io.printlnCommand("Collection cleared successfully");

        } else {
            io.printlnCommand("Command cancelled");
        }
    }
}
