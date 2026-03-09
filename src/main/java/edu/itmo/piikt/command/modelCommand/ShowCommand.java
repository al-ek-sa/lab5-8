package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class ShowCommand implements FindWorker, BaseSimpleCommand {
    public ShowCommand() {
    }

    /** The method outputs data of all registered employees. */
    @Override
    public void doExecute(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        findWorker(io);
        list.forEach(worker -> io.println(worker.toString()));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.SHOW;
    }
}
