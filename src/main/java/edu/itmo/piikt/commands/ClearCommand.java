package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.managers.Confirmation;
import edu.itmo.piikt.validationModels.GeneratorId;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ClearCommand extends Commands implements Confirmation {
    private IOProvider io;
    private HistoryWorker historyWorker;
    public ClearCommand(IOProvider io){
        super("clear");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }

    @Override
    public void execute() {
        if(io.name().equals("Console")){
            io.printeDesign();
            io.printlnCommand("Are you sure you want to clear the collection? (yes/no)"); //Вы точно хотите очистить коллекцию? (да/нет)
            io.printeDesign();
            String consent = confirmation();
            if (consent.equals("yes")){
                io.printeDesign();
                io.printlnCommand("Consent received, clearing collection");
                io.printeDesign();
                historyWorker.clear();
                GeneratorId.getInstance(io).setStartId(1);
                io.printeDesign();
                io.printlnCommand("Collection cleared successfully");
                io.printeDesign();
            } else {
                io.printeDesign();
                io.printlnCommand("Consent received, clearing collection");
                GeneratorId.getInstance(io).setStartId(1);
                io.printeDesign();
            }
        }

        if (io.name().equals("File")){
            io.printeDesign();
            io.printlnCommand("Consent received, clearing collection");
            historyWorker.clear();
            GeneratorId.getInstance(io).setStartId(1);
            io.printlnCommand("Collection cleared successfully");
            io.printeDesign();

        } else {
            io.printeDesign();
            io.printlnCommand("Command cancelled");
            io.printeDesign();
        }
    }

    @Override
    public String confirmation(){
        try {
            while (true) {
                String input = io.readLine();
                if (input.equals("yes")) {
                    return "yes";
                } else if (input.equals("no")) {
                    return "no";
                }
                io.printeDesign();
                io.printException("Please enter 'yes' or 'no'"); //пожалуйста введите да или нет
                io.printeDesign();
                //коллекция успешно очищена
                io.printlnCommand("Collection successfully cleared");
                io.printeDesign();
            }
        }catch (Exception e) {
            io.printeDesign();
            //очистка коллекции не удалась
            io.printException("Failed to clear the collection");
            io.printeDesign();
            return null;
        }
    }
}
