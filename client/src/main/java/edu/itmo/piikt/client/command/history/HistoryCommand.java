package edu.itmo.piikt.client.command.history;

import edu.itmo.piikt.common.io.provider.IOProvider;
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
    //todo
    public void execute(IOProvider io) {
        var history = HistoryCommands.INSTANCE.getLinkedList();
        List<String> list = history.stream().limit(LIMIT_HISTORY).collect(Collectors.toList());
        list.forEach(io::println);
    }
}
