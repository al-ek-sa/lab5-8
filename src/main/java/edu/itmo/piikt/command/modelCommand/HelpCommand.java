package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.command.data.Commands;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command help : display help on available commands.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see IOProvider
 * @see Commands
 */

// public class HelpCommand implements Command {
public final class HelpCommand implements BaseSimpleCommand {

    public HelpCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        for (Commands commands : Commands.values()) {
            io.println(commands.getName() + ": " + commands.getDescription());
        }
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.HELP;
    }
}
