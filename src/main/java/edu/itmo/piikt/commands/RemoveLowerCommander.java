package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

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
            io.println("Deletion of items started");
            io.printeDesign();
            HistoryWorker.getInstance(io).removeLower(argument);
            io.printeDesign();
            //элементы успешно удалены
            io.println("Items successfully deleted");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //отказано в удалении элементов
            io.printError("Items deletion denied");
            io.printeDesign();
        }
    }
}
