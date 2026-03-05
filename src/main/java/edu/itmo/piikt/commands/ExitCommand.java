package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Confirmation;
import edu.itmo.piikt.managers.SimpleCommand;
import edu.itmo.piikt.managers.ValidationCommand;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ExitCommand implements SimpleCommand, Confirmation {
    Logger logger = Logger.getLogger(ExitCommand.class.getName());

    public ExitCommand() {
    }

    public void execute(IOProvider io) {
        try {
            if (io.name().equals("Console")) {
                io.printlnCommand("Are you sure you want to exit? (yes/no)");
                Boolean consent = confirmation(io);
                if (consent == true) {
                    logger.log(Level.INFO, "Exit application");
                    ValidationCommand.getInstance().setFlag(false);
                } else {
                    logger.log(Level.INFO, "Command cancelled");
                }
            }

            if (io.name().equals("File")) {
                logger.log(Level.INFO, "Exit application");
                ValidationCommand.getInstance().setFlag(false);
            }

        } catch (Exception e) {
            io.printException("Command not executed");
        }
    }
}
