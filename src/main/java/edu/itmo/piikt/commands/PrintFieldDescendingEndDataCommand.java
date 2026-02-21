package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class PrintFieldDescendingEndDataCommand extends Commands {
    private IOProvider io;
    public PrintFieldDescendingEndDataCommand(IOProvider io) {
        super("print_field_descending_end_date");
        this.io = io;
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance(io).sort();
    }
}
