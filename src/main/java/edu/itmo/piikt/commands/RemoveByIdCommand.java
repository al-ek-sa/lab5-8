package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class RemoveByIdCommand extends Commands{
    public RemoveByIdCommand(){
        super("remove_by_id");
    }
    @Override
    public void execute() {
        HistoryWorker.getInstance().removeId();
    }
}
