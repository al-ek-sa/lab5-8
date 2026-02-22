package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

public class RemoveByIdCommand extends ArgumentCommand {
    private IOProvider io;
    public RemoveByIdCommand(IOProvider io){
        super("remove_by_id id");
        this.io = io;
    }
    @Override
    public void execute(String argument) {
        try{
            io.printeDesign();
            //удаление элемента по id началось
            io.println("Deletion of item by ID started");
            io.printeDesign();
            int idConsole = Integer.parseInt(argument);
            HistoryWorker.getInstance(io).removeId(idConsole);
            io.printeDesign();
            //элемент успешно удален
            io.println("Item successfully deleted");
            io.printeDesign();
        } catch (RuntimeException e) {
            io.printeDesign();
            //В аргументе введены посторонние символы, повторите команду (аргумент может содержать только целые числа больше 0)
            io.printError("Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
            io.printeDesign();
        }

    }
}
