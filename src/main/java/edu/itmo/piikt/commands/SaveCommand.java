package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.reader.CSVParser;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command save : save the collection to a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class SaveCommand {
    Logger logger = Logger.getLogger(SaveCommand.class.getName());
    public SaveCommand(){}

    public void execute(IOProvider io) {
        try {
            logger.log(Level.INFO,"Saving data to file started");
            io.printeDesign();
            CSVParser csvParser = new CSVParser(io);
            csvParser.saveCollection();
        } catch (Exception e) {
            logger.log(Level.INFO,"Data saved to file");
        }
    }
}
