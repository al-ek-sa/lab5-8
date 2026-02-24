package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

/**
 * The class implements the command filter_contains_name name : output elements whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class FilterContainsNameCommand extends ArgumentCommand {
    private IOProvider io;
    public FilterContainsNameCommand(IOProvider io){
        super("filter_contains_name");
        this.io = io;
    }

    @Override
    public void execute(String argument) {
        try {
            io.printeDesign();
            io.printlnCommand("Search users by name");
            io.printeDesign();
            HistoryWorker.getInstance(io).printName(argument);
            io.printeDesign();
            io.printlnCommand("All users with the entered name have been displayed");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //поиск неудался
            io.printException("Search failed");
            io.printeDesign();
        }
    }
}
