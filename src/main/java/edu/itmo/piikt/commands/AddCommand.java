package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.validationModels.ValidationWorker;

//public class AddCommand implements Command {
public class AddCommand extends Commands {
    private ValidationWorker worker;
    public AddCommand(){
        super("add");
        this.worker = new ValidationWorker();
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance().add(worker.worker());
    }

}
