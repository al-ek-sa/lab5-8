package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.command.data.Commands;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command help_entering_command : display help on
 * entering available commands
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 *
 * @see IOProvider
 * @see BaseSimpleCommand
 * @see Commands
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
