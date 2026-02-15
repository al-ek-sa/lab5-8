package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class UpdateIdCommand extends Commands {
    public UpdateIdCommand(){
        super("update id");
    }
    @Override
    public void execute() {
        HistoryWorker.getInstance().update();
    }
}
