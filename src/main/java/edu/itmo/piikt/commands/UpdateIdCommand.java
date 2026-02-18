package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class UpdateIdCommand extends Commands {
    private IOProvider io;
    public UpdateIdCommand(IOProvider io){
        super("update id");
        this.io = io;
    }
    @Override
    public void execute() {
        HistoryWorker.getInstance(io).update();
    }
}
