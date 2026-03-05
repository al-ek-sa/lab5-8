package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

/**
 * The class implements the command help_entering_command : display help on
 * entering available commands
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class HelpEnteringCommand {
    public HelpEnteringCommand() {
    }

    public void execute(IOProvider io) {
        for (Commands commands : Commands.values()) {
            io.println(commands.getName() + ": " + commands.getHelp());
        }
    }
}
