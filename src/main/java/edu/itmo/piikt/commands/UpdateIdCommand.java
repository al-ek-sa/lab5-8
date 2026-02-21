package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

public class UpdateIdCommand extends ArgumentCommand {
    private IOProvider io;
    public UpdateIdCommand(IOProvider io){
        super("update_id");
        this.io = io;
    }
    @Override
    public void execute(String argument) {

        HistoryWorker.getInstance(io).update(argument);
    }
}
