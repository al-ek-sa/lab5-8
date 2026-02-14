package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class InfoCommand extends Commands{
    public InfoCommand(){
        super("info");
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance().infoLiat();
    }
}
