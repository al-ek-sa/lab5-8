package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.Commands;
import java.util.logging.Logger;

/**
 * The class implements the command help : display help on available commands.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */

// public class HelpCommand implements Command {
public final class HelpCommand implements BaseSimpleCommand {
    Logger logger = Logger.getLogger(HelpCommand.class.getName());

    public HelpCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        for (Commands commands : Commands.values()) {
            io.println(commands.getName() + ": " + commands.getDescription());
        }
    }
}
