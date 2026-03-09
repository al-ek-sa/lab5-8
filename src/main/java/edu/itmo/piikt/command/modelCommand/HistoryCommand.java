package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.history.HistoryCommands;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command history : output the last 14 commands
 * (without their arguments).
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class HistoryCommand implements BaseSimpleCommand {
    int LIMIT_HISTORY = 14;
    public HistoryCommand() {
    }

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
