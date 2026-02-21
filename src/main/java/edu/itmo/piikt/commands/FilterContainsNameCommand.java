package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

public class FilterContainsNameCommand extends ArgumentCommand {
    private IOProvider io;
    public FilterContainsNameCommand(IOProvider io){
        super("filter_contains_name");
        this.io = io;
    }

    @Override
    public void execute(String argument) {
        String input = "-".repeat(50);
        io.println(input);
        io.println("Search users by name");
        HistoryWorker.getInstance(io).printName(argument);
        io.println("All users with the entered name have been displayed");
        io.println(input);
    }
}
