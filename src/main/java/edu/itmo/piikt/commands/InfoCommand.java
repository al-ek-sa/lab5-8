package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class InfoCommand extends Commands{
    private IOProvider io;
    public InfoCommand(IOProvider io){
        super("info");
        this.io = io;
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance(io).infoLiat();
    }
}
