package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class HeadCommand extends Commands {
    private HistoryWorker historyWorker;
    private IOProvider io;
    public HeadCommand(IOProvider io){
        super("head");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            io.printlnCommand("Displaying the last added element");
            io.printeDesign();
            historyWorker.peekFirst();
            io.printeDesign();
            io.printlnCommand("Element displayed on the screen");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //команда не выполнена
            io.printException("Command not executed");
            io.printeDesign();
        }
    }
}
