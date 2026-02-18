package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class HeadCommand extends Commands{
    private HistoryWorker historyWorker;
    private IOProvider io;
    public HeadCommand(IOProvider io){
        super("head");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }

    @Override
    public void execute() {
        String input = "-".repeat(50);
        io.println(input);
        io.println("Displaying the last added element");
        historyWorker.peekFirst();
        io.println("Element displayed on the screen");
        io.println(input);
    }
}
