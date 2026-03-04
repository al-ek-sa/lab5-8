package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.validationModels.ValidationWorker;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The class implements the command add {element} : add a new element to the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

//public class AddCommand implements Command {
public class AddCommand {
    Logger logger = Logger.getLogger(AddCommand.class.getName());
    public AddCommand(){}

    public void execute(IOProvider io) {
        ValidationWorker worker = new ValidationWorker(io);
        try {
            logger.log(Level.INFO,"Start adding an item");
            HistoryWorker.getInstance(io).add(worker.worker());
            logger.log(Level.INFO,"Item successfully added");
        } catch (RuntimeException e){
            logger.log(Level.INFO, e.getMessage());
        }
    }

}
