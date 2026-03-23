package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command help_entering_command : display help on
 * entering available commands
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see Commands
 */
@NoArgsConstructor
public final class HelpEnteringCommand {
    public ServerResponse execute() {
        List<String> list = Arrays.stream(Commands.values()).sorted(Comparator.comparing(Commands::getName))
                .map(commands -> commands.getName() + ": " + commands.getHelp()).collect(Collectors.toList());
        return ServerResponse.successfulCompletion("Help: ", list);
    }
}
