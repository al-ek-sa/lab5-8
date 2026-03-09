package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.HistoryCommands;
import edu.itmo.piikt.managers.MessageCommand;

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
