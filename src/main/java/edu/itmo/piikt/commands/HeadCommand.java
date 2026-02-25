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

public class HeadCommand extends Commands {
    private HistoryWorker historyWorker;
    private IOProvider io;
    Logger logger = Logger.getLogger(HeadCommand.class.getName());
    public HeadCommand(IOProvider io){
        super("head");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            logger.log(Level.INFO,"Displaying the last added element");
            io.printeDesign();
            historyWorker.peekFirst();
            io.printeDesign();
            logger.log(Level.INFO,"Element displayed on the screen");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //команда не выполнена
            io.printException("Command not executed");
            io.printeDesign();
        }
    }
}
