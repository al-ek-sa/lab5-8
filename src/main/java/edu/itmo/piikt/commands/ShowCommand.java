package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.MessageCommand;

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
