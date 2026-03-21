package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.massage.MessageCommand;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public final class HelpCommand implements BaseSimpleCommand {
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
