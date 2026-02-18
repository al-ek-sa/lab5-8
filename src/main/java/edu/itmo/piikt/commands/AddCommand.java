package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.validationModels.ValidationWorker;

//public class AddCommand implements Command {
public class AddCommand extends Commands {
    private IOProvider io;
    private ValidationWorker worker;
    public AddCommand(IOProvider io){
        super("add");
        this.io = io;
        this.worker = new ValidationWorker(io);
    }

    @Override
    public void execute() {
        String input = "-".repeat(70);
        io.println(input);
        io.println("adding an element to the employee list");
        HistoryWorker.getInstance(io).add(worker.worker());
        io.println("command executed successfully, worker added to the collection");
        io.println(input);
    }

}
