package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryCommands;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command history : output the last 14 commands
 * (without their arguments).
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryCommands
 */
@NoArgsConstructor
public final class HistoryCommand {
    int LIMIT_HISTORY = 14;
    public ServerResponse execute() {
        var history = HistoryCommands.INSTANCE.getLinkedList();
        List<String> list = history.stream().limit(LIMIT_HISTORY).collect(Collectors.toList());
        return ServerResponse.successfulCompletion("список команд: ", list);
    }
}
