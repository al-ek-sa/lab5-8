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

public class ExitCommand implements Confirmation {
    Logger logger = Logger.getLogger(ExitCommand.class.getName());
    public ExitCommand(){}

    public void execute(IOProvider io) {
        try {
            if(io.name().equals("Console")){
                io.printlnCommand("Are you sure you want to exit? (yes/no)");
                String consent = confirmation(io);
                if (consent.equals("yes")) {
                    logger.log(Level.INFO,"Exit application");
                    System.exit(0);
                } else {
                    logger.log(Level.INFO,"Command cancelled");
                }
            }

            if (io.name().equals("File")){
                logger.log(Level.INFO,"Exit application");
                System.exit(0);
            }

            } catch (Exception e) {
                io.printException("Command not executed");
            }
    }

    @Override
    public String confirmation(IOProvider io){
        while (true){
            String  input = io.readLine();
            if (input.equals("yes")){
                return "yes";
            } else if (input.equals("no")) {
                return "no";
            }
            io.printlnCommand("Please enter 'yes' or 'no'");
        }
    }
}
