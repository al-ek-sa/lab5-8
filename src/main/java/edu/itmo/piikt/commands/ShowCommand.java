package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class ShowCommand extends Commands {
    private IOProvider io;
    private HistoryWorker historyWorker;
    public ShowCommand(IOProvider io){
        super("show");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }
    @Override
    public void execute() {
        historyWorker.printHistoryWorker();
    }
}
