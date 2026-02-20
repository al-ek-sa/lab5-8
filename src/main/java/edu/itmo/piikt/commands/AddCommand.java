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
        try {
            HistoryWorker.getInstance(io).add(worker.worker());
        } catch (RuntimeException e){
            io.printError("в скрипте внесены неправильные данные");
        }
    }

}
