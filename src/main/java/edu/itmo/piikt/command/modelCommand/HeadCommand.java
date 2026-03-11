package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command head : output the first element of the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see FindWorker
 * @see BaseSimpleCommand
 * @see IOProvider
 * @see HistoryWorker
 */
public final class HeadCommand implements FindWorker, BaseSimpleCommand {
    public HeadCommand() {
    }
    /** The method outputs the data of the first element in the collection. */
    @Override
    public void doExecute(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        if (findWorker(io)) {
            return;
        }
        io.println(list.getFirst().toString());
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.HEAD;
    }
}
