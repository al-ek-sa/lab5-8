package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.Confirmation;
import edu.itmo.piikt.managers.ValidationCommand;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class ExitCommand implements Confirmation, BaseSimpleCommand {
    Logger logger = Logger.getLogger(ExitCommand.class.getName());

    public ExitCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        Boolean consent = confirmation(io);
        if (consent == true) {
            ValidationCommand.getInstance().setFlag(false);
        }
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Exit application");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Exit application");
    }

    @Override
    public void question(IOProvider io) {
        io.printlnCommand("Are you sure you want to exit? (yes/no)");
    }

    @Override
    public void refusal() {
        logger.log(Level.INFO, "Command cancelled");
    }
}
