package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class ShowCommand extends Commands {
    private IOProvider io;
    private HistoryWorker historyWorker;
    public ShowCommand(IOProvider io){
        super("show");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }
    @Override
    public void execute() {
        try {
            io.printeDesign();
            //отображение коллекции
            io.printlnCommand("Displaying collection");
            io.printeDesign();
            historyWorker.printHistoryWorker();
            io.printeDesign();
            //Displaying collection
            io.printlnCommand("Collection displayed");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //Отображение коллекции прервано
            io.printException("Displaying collection interrupted");
            io.printeDesign();
        }
    }
}
