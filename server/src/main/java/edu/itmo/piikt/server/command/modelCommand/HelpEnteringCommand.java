package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
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
 * @version 3.1
 * @see Commands
 */
@NoArgsConstructor
public final class HelpEnteringCommand {
    private static final AppLogger logger = new AppLogger(HelpEnteringCommand.class);

    public ServerResponse execute() {
        try (Context context = Context.newId()) {
            logger.info("Executing HELP_ENTERING_COMMAND");
            List<String> list = Arrays.stream(Commands.values())
                    .sorted(Comparator.comparing(Commands::getName))
                    .map(commands -> commands.getName() + ": " + commands.getHelp())
                    .collect(Collectors.toList());
            logger.debug("Help entries: {}", list.size());
            return ServerResponse.successfulCompletion("Help: ", list);
        } catch (Exception e) {
            logger.error("Error executing HELP_ENTERING_COMMAND: {}", e);
            throw new RuntimeException(e);
        }
    }
}