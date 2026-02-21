package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.managers.Confirmation;

public class ExitCommand extends Commands implements Confirmation {
    private IOProvider io;
    public ExitCommand(IOProvider io){
        super("exit");
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //вы уверены, что хотите выйти?
            io.println("Are you sure you want to exit? (yes/no)");
            String consent = confirmation();
            if (consent.equals("yes")) {
                io.printeDesign();
                io.println("Exit application");
                io.printeDesign();
                System.exit(0);
            } else {
                io.printeDesign();
                io.println("Command cancelled");
                io.printeDesign();
            }
        } catch (Exception e) {
            io.printeDesign();
            //команда не выполнена
            io.printError("Command not executed");
            io.printeDesign();
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
