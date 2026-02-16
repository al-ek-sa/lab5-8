package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class RemoveLowerCommander extends Commands{
    public RemoveLowerCommander(){
        super("remove_lower");
    }
    @Override
    public void execute() {
        HistoryWorker.getInstance().removeLower();
    }
}
