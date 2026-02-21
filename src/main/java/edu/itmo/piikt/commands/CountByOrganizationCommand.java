package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class CountByOrganizationCommand extends Commands {
    private IOProvider io;
    public  CountByOrganizationCommand(IOProvider io){
        super("count_by_organization");
        this.io =io;
    }

    @Override
    public void execute() {
        String input = "-".repeat(50);
        io.println(input);
        io.println("Enter all values for Organization");
        HistoryWorker.getInstance(io).countByOrganization();
        io.println("Number of elements displayed successfully");
        io.println(input);
    }
}
