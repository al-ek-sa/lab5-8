package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.MessageCommand;

/**
 * The class implements the command info : output information about the
 * collection to the standard output stream (type, initialization date, number
 * of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class InfoCommand implements BaseSimpleCommand {
    public InfoCommand() {
    }
    @Override
    /** The method outputs data about the collection. */
    public void doExecute(IOProvider io) {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        var data = HistoryWorker.INSTANCE.getData();
        io.println("Collection type: " + listWorker.getClass() + "\nIdentification time: " + data
                + "\nNumber of elements: " + listWorker.size());

    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.INFO;
    }
}
