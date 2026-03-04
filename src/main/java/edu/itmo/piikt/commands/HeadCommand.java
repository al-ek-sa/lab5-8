package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The class implements the command head : output the first element of the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class HeadCommand {
    Logger logger = Logger.getLogger(HeadCommand.class.getName());
    public HeadCommand(){
    }

    public void execute(IOProvider io) {
        try {
            logger.log(Level.INFO,"Displaying the last added element");
            HistoryWorker.getInstance(io).peekFirst();
        } catch (Exception e) {
            io.printException("Command not executed");
        }
    }
}
