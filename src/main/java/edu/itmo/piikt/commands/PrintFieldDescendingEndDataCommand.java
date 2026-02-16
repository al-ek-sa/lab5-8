package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class PrintFieldDescendingEndDataCommand extends  Commands {
    public PrintFieldDescendingEndDataCommand() {
        super("print_field_descending_end_date");
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance().sort();
    }
}
