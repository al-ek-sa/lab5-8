package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.client.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
import lombok.NoArgsConstructor;

/**
 * The class implements the command info : output information about the
 * collection to the standard output stream (type, initialization date, number
 * of elements, etc.).
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see IOProvider
 * @see HistoryWorker
 * @see BaseSimpleCommand
 */
@NoArgsConstructor
public final class InfoCommand implements BaseSimpleCommand {
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
