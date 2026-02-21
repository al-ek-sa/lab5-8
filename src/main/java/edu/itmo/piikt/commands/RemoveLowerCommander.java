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
        HistoryWorker.getInstance(io).removeLower(argument);
    }
}
