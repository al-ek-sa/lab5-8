package edu.itmo.piikt.client.command.history;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The class implements the command history : output the last 14 commands
 * (without their arguments).
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryCommands
 */
@NoArgsConstructor
public final class HistoryCommand {
    private static final int LIMIT_HISTORY = 14;
    private static final AppLogger logger = new AppLogger(HistoryCommand.class);
    public void execute(IOProvider io) {
        try (Context ignored = Context.newId()) {
            logger.debug("Executing history command");
            var history = HistoryCommands.INSTANCE.getLinkedList();
            logger.debug("Total commands in history: {}", history.size());
            List<String> list = history.stream()
                    .limit(LIMIT_HISTORY)
                    .toList();
            logger.info("Displaying last {} commands", list.size());
            list.forEach(io::println);
            logger.debug("History command completed");
        } catch (Exception e) {
            logger.error("Error executing history command: {}", e);
            io.println("Error: Failed to retrieve command history");
        }
    }
}