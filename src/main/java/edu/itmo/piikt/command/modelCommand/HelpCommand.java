package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.command.data.Commands;
import edu.itmo.piikt.massage.MessageCommand;

import java.util.Arrays;
import java.util.Comparator;

/**
 * The class implements the command help : display help on available commands.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see IOProvider
 * @see Commands
 */

public final class HelpCommand implements BaseSimpleCommand {

    public HelpCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        Arrays.stream(Commands.values()).sorted(Comparator.comparing(Commands::getName))
                .map(commands -> commands.getName() + ": " + commands.getDescription()).forEach(io::println);
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.HELP;
    }
}
