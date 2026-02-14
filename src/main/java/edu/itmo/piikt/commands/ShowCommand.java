package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class ShowCommand extends Commands {
    private HistoryWorker historyWorker;
    public ShowCommand(){
        super("show");
        this.historyWorker = HistoryWorker.getInstance();
    }
    @Override
    public void execute() {
        historyWorker.printHistoryWorker();
    }
}
