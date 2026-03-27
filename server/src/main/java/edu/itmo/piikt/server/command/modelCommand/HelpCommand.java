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
 * The class implements the command help : display help on available commands.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see Commands
 */
@NoArgsConstructor
public final class HelpCommand {
    private static final AppLogger logger = new AppLogger(HelpCommand.class);

    public ServerResponse execute() {
        try (Context context = Context.newId()) {
            logger.info("Executing HELP command");
            List<String> list = Arrays.stream(Commands.values())
                    .sorted(Comparator.comparing(Commands::getName))
                    .map(commands -> commands.getName() + ": " + commands.getDescription())
                    .collect(Collectors.toList());
            logger.debug("Available commands: {}", list.size());
            return ServerResponse.successfulCompletion("HELP: ", list);
        } catch (Exception e) {
            logger.error("Error executing HELP command: {}", e);
            throw new RuntimeException(e);
        }
    }
}