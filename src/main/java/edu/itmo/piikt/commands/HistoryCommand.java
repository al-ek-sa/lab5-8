package edu.itmo.piikt.commands;

import java.util.Iterator;

public class HistoryCommand extends Commands{
    private HistoryCommands historyCommands;
    public HistoryCommand(){
        super("history");
        this.historyCommands = HistoryCommands.getInstance();
    }

    @Override
    public void execute() {
        Iterator<String> iterator = historyCommands.getIterator();
        for (int i = 1; i <= 14 && iterator.hasNext(); i++) {
            String command = iterator.next();
            System.out.println(command);
        }
    }
}
