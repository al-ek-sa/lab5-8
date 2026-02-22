package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.managers.HistoryCommands;

import java.util.Iterator;

public class HistoryCommand extends Commands {
    private HistoryCommands historyCommands;
    private IOProvider io;
    public HistoryCommand(IOProvider io){
        super("history");
        this.historyCommands = HistoryCommands.getInstance();
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            io.println("Displaying the last 14 commands");
            io.printeDesign();
            Iterator<String> iterator = historyCommands.getIterator();
            for (int i = 1; i <= 14 && iterator.hasNext(); i++) {
                String command = iterator.next();
                io.println(command);
            }
            io.printeDesign();
            io.println("Commands displayed successfully");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //команда не выполнена
            io.printError("Command not executed");
            io.printeDesign();
        }
    }
}
