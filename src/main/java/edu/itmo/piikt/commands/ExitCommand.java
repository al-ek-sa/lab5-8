package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.managers.Confirmation;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command exit : terminate the program (without saving to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExitCommand extends Commands implements Confirmation {
    private IOProvider io;
    Logger logger = Logger.getLogger(ExitCommand.class.getName());
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
                    logger.log(Level.INFO,"Exit application");
                    io.printeDesign();
                    System.exit(0);
                } else {
                    io.printeDesign();
                    logger.log(Level.INFO,"Command cancelled");
                    io.printeDesign();
                }
            }

            if (io.name().equals("File")){
                io.printeDesign();
                logger.log(Level.INFO,"Exit application");
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
