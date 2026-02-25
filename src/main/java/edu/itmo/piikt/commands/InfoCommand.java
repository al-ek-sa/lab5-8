package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command info : output information about the collection to the standard output stream (type, initialization date, number of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class InfoCommand extends Commands {
    private IOProvider io;
    Logger logger = Logger.getLogger(InfoCommand.class.getName());
    public InfoCommand(IOProvider io){
        super("info");
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //отображение информации про коллекцию
            logger.log(Level.INFO,"Displaying information about the collection");
            io.printeDesign();
            HistoryWorker.getInstance(io).infoLiat();
            io.printeDesign();
            logger.log(Level.INFO,"Information successfully displayed");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //информация не отображена
            io.printException("Information not displayed");
            io.printeDesign();
        }
    }
}
