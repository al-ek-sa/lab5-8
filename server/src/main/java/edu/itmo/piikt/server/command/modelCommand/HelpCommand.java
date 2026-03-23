package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command help : display help on available commands.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see Commands
 */
@NoArgsConstructor
public final class HelpCommand {

    public ServerResponse execute() {
        List<String> list = Arrays.stream(Commands.values()).sorted(Comparator.comparing(Commands::getName))
                .map(commands -> commands.getName() + ": " + commands.getDescription()).collect(Collectors.toList());
        return ServerResponse.successfulCompletion("HELP: ", list);
    }
}
