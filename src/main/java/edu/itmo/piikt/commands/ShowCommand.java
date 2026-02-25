package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command show : output all elements of the collection in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ShowCommand extends Commands {
    private IOProvider io;
    private HistoryWorker historyWorker;
    Logger logger = Logger.getLogger(ShowCommand.class.getName());
    public ShowCommand(IOProvider io){
        super("show");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }
    @Override
    public void execute() {
        try {
            io.printeDesign();
            //отображение коллекции
            logger.log(Level.INFO,"Displaying collection");
            io.printeDesign();
            historyWorker.printHistoryWorker();
            io.printeDesign();
            //Displaying collection
            logger.log(Level.INFO,"Collection displayed");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //Отображение коллекции прервано
            logger.log(Level.INFO,"Displaying collection interrupted");
            io.printeDesign();
        }
    }
}
