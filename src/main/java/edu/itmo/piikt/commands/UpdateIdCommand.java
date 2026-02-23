package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;
import edu.itmo.piikt.validationModels.ValidationWorker;

public class UpdateIdCommand extends ArgumentCommand {
    private IOProvider io;
    private ValidationWorker worker;
    public UpdateIdCommand(IOProvider io){
        super("update");
        this.io = io;
        this.worker = new ValidationWorker(io);
    }

    @Override
    public void execute(String argument) {
        try {
            io.printeDesign();
            //начало обновления данных
            io.printlnCommand("Start of data update");
            HistoryWorker.getInstance(io).idMatches(argument);
            HistoryWorker.getInstance(io).update(argument, worker);
            io.printeDesign();
            //данные успешно обновлены
            io.printlnCommand("Data successfully updated");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //Обновление данных прервано
            io.printException("Data update interrupted");
            io.printeDesign();
        }
    }
}
