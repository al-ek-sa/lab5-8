package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.history.HistoryCommands;
import edu.itmo.piikt.massage.MessageCommand;
import lombok.NoArgsConstructor;

/**
 * The class implements the command history : output the last 14 commands
 * (without their arguments).
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see IOProvider
 * @see HistoryCommands
 */
@NoArgsConstructor
public final class HistoryCommand implements BaseSimpleCommand {
    int LIMIT_HISTORY = 14;
    // todo читать с конца
    @Override
    public void doExecute(IOProvider io) {
        var history = HistoryCommands.INSTANCE.getLinkedList();
        history.stream().limit(LIMIT_HISTORY).forEach(io::println);
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.HISTORY;
    }
}
