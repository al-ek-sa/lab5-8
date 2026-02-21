package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

public class UpdateIdCommand extends ArgumentCommand {
    private IOProvider io;
    public UpdateIdCommand(IOProvider io){
        super("update_id");
        this.io = io;
    }
    @Override
    public void execute(String argument) {
        try {
            io.printeDesign();
            //начало обновления данных
            io.println("Start of data update");
            HistoryWorker.getInstance(io).update(argument);
            io.printeDesign();
            //данные успешно обновлены
            io.println("Data successfully updated");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //Обновление данных прервано
            io.printError("Data update interrupted");
            io.printeDesign();
        }
    }
}
