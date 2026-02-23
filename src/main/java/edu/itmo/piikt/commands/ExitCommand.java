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
            if(io.name().equals("Console")){
                io.printeDesign();
                //вы уверены, что хотите выйти?
                io.printlnCommand("Are you sure you want to exit? (yes/no)");
                io.printeDesign();
                String consent = confirmation();
                if (consent.equals("yes")) {
                    io.printeDesign();
                    io.printlnCommand("Exit application");
                    io.printeDesign();
                    System.exit(0);
                } else {
                    io.printeDesign();
                    io.printlnCommand("Command cancelled");
                    io.printeDesign();
                }
            }

            if (io.name().equals("File")){
                io.printeDesign();
                io.printlnCommand("Exit application");
                System.exit(0);
            }

            } catch (Exception e) {
                io.printeDesign();
                //команда не выполнена
                io.printException("Command not executed");
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
            }
            io.printeDesign();
            io.printlnCommand("Please enter 'yes' or 'no'");
            io.printeDesign();
        }
    }
}
