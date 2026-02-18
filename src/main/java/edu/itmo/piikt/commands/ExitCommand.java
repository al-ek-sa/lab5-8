package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;

public class ExitCommand extends Commands implements Confirmation{
    private IOProvider io;
    public ExitCommand(IOProvider io){
        super("exit");
        this.io = io;
    }

    @Override
    public void execute() {
        String input = "-".repeat(50);
        io.println(input);
        io.println("Are you sure you want to exit? (yes/no)");
        String consent = confirmation();
        if (consent.equals("yes")){
            io.println("Exit application");
            System.exit(0);
        } else {
            io.println("Command cancelled");
            io.println(input);
        }
    }

    @Override
    public String confirmation(){
        while (true){
            String  input = io.readLIne();
            if (input.equals("yes")){
                return "yes";
            } else if (input.equals("no")) {
                return "no";
            } io.println("Please enter 'yes' or 'no'");
        }
    }
}
