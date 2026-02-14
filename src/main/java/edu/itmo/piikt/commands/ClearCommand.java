package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class ClearCommand extends  Commands{
    private HistoryWorker historyWorker;
    public ClearCommand(){
        super("clear");
        this.historyWorker = HistoryWorker.getInstance();
    }

    @Override
    public void execute() {
        historyWorker.clear();
    }
}
