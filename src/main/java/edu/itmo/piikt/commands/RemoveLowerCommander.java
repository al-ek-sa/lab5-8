package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class RemoveLowerCommander extends Commands{
    private IOProvider io;
    public RemoveLowerCommander(IOProvider io){
        super("remove_lower");
        this.io = io;
    }
    @Override
    public void execute() {
        HistoryWorker.getInstance(io).removeLower();
    }
}
