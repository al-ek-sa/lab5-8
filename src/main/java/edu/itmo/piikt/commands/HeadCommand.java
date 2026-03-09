package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.MessageCommand;

/**
 * The class implements the command head : output the first element of the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class HeadCommand implements FindWorker, BaseSimpleCommand {
    public HeadCommand() {
    }
    /** The method outputs the data of the first element in the collection. */
    @Override
    public void doExecute(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        findWorker(io);
        io.println(list.getFirst().toString());
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.HEAD;
    }
}
