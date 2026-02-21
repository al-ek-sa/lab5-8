package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
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
            io.printeDesign();
            //Начало добавления элемента
            io.println("Start adding an item");
            io.printeDesign();
            HistoryWorker.getInstance(io).add(worker.worker());
            io.printeDesign();
            //Элемент успешно добавлен
            io.println("Item successfully added");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //добавить элемент не удалось
            io.printError("Failed to add item");
            io.printeDesign();
        }
    }

}
