package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.reader.CSVParser;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command save : save the collection to a file.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class SaveCommand implements BaseSimpleCommand {
    Logger logger = Logger.getLogger(SaveCommand.class.getName());

    public SaveCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        CSVParser csvParser = new CSVParser();
        csvParser.saveCollection();
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Saving data to file started");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Data saved to file");
    }
}
