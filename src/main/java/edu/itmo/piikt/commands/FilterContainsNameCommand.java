package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class FilterContainsNameCommand extends Commands{
    public FilterContainsNameCommand(){
        super("filter_contains_name");
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance().printName();
    }
}
