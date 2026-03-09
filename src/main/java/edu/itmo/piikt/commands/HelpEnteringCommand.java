package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.managers.MessageCommand;

/**
 * The class implements the command help_entering_command : display help on
 * entering available commands
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class HelpEnteringCommand implements BaseSimpleCommand {
    public HelpEnteringCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        for (Commands commands : Commands.values()) {
            io.println(commands.getName() + ": " + commands.getHelp());
        }
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.HELP_ENTERING;
    }
}
