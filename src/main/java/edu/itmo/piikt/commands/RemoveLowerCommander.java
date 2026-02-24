package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

/**
 * The class implements the command remove_lower {element} : remove from the collection all
 * elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class RemoveLowerCommander extends ArgumentCommand {
    private IOProvider io;
    public RemoveLowerCommander(IOProvider io){
        super("remove_lower");
        this.io = io;
    }
    @Override
    public void execute(String argument) {
        try {
            io.printeDesign();
            //удаление элементов началось
            io.printlnCommand("Deletion of items started");
            io.printeDesign();
            HistoryWorker.getInstance(io).removeLower(argument);
            io.printeDesign();
            //элементы успешно удалены
            io.printlnCommand("Items successfully deleted");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //отказано в удалении элементов
            io.printException("Items deletion denied");
            io.printeDesign();
        }
    }
}
