package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class HeadCommand extends Commands{
    private HistoryWorker historyWorker;
    public HeadCommand(){
        super("head");
        this.historyWorker = HistoryWorker.getInstance();
    }

    @Override
    public void execute() {
        historyWorker.peekFirst();
    }
}
