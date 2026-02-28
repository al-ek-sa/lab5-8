package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_lower {element} : remove from the collection all
 * elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class RemoveLowerCommander extends ArgumentCommand {
    private IOProvider io;
    Logger logger = Logger.getLogger(RemoveLowerCommander.class.getName());
    public RemoveLowerCommander(IOProvider io){
        super("remove_lower");
        this.io = io;
    }
    @Override
    public void execute(String argument) {
        try {
            HistoryWorker.getInstance(io).removeLower(argument);
        } catch (RuntimeException e){
            io.printeDesign();
            //отказано в удалении элементов
            logger.log(Level.INFO,"Items deletion denied");
            io.printeDesign();
        }
    }
}
