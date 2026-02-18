package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class ClearCommand extends  Commands implements Confirmation {
    private IOProvider io;
    private HistoryWorker historyWorker;
    public ClearCommand(IOProvider io){
        super("clear");
        this.historyWorker = HistoryWorker.getInstance(io);
        this.io = io;
    }

    @Override
    public void execute() {
        String input = "-".repeat(50);
        io.println(input);
        io.println("Are you sure you want to clear the collection? (yes/no)");
        String consent = confirmation();
        if (consent.equals("yes")){
            io.println("Consent received, clearing collection");
            historyWorker.clear();
            io.println("Collection cleared successfully");
            io.println(input);
        } else {
            io.println("Command cancelled");
            io.println(input);
        }
    }

    @Override
    public String confirmation(){
        while (true){
            String  input = io.readLine();
            if (input.equals("yes")){
                return "yes";
            } else if (input.equals("no")) {
                return "no";
            } io.println("Please enter 'yes' or 'no'");
        }
    }
}
