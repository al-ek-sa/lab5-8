package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class RemoveByIdCommand extends ArgumentCommand{
    private IOProvider io;
    public RemoveByIdCommand(IOProvider io){
        super("remove_by_id");
        this.io = io;
    }
    @Override
    public void execute(String argument) {
        try{
            int idConsole = Integer.parseInt(argument);
            HistoryWorker.getInstance(io).removeId(idConsole);
        } catch (RuntimeException e) {
            io.printException("Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
        }

    }
}
