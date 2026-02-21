package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class InfoCommand extends Commands {
    private IOProvider io;
    public InfoCommand(IOProvider io){
        super("info");
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //отображение информации про коллекцию
            io.println("Displaying information about the collection");
            io.printeDesign();
            HistoryWorker.getInstance(io).infoLiat();
            io.printeDesign();
            io.println("Information successfully displayed");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //информация не отображена
            io.printError("Information not displayed");
            io.printeDesign();
        }
    }
}
