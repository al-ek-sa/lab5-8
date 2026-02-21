package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.managers.Confirmation;

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
        io.printeDesign();
        io.println("Are you sure you want to clear the collection? (yes/no)"); //Вы точно хотите очистить коллекцию? (да/нет)
        io.printeDesign();
        String consent = confirmation();
        if (consent.equals("yes")){
            io.println("Consent received, clearing collection");
            historyWorker.clear();
            io.println("Collection cleared successfully");
            io.printeDesign();
        } else {
            io.printeDesign();
            io.println("Command cancelled");
            io.printeDesign();
        }
    }

    @Override
    public String confirmation(){
        try {
            while (true) {
                io.printeDesign();
                //Команда начинает свое выполнение
                io.println("The command is starting its execution");
                String input = io.readLine();
                if (input.equals("yes")) {
                    return "yes";
                } else if (input.equals("no")) {
                    return "no";
                }
                io.println("Please enter 'yes' or 'no'"); //пожалуйста введите да или нет
                io.printeDesign();
                //коллекция успешно очищена
                io.println("Collection successfully cleared");
                io.printeDesign();
            }
        }catch (Exception e) {
            io.printeDesign();
            //очистка коллекции не удалась
            io.printError("Failed to clear the collection");
            io.printeDesign();
            return null;
        }
    }
}
