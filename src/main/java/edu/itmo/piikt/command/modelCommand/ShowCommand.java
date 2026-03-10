package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.interfaces.FindWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.models.Worker;

import java.util.Comparator;

/**
 * The class implements the command show : output all elements of the collection
 * in string representation to the standard output stream.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see FindWorker
 * @see BaseSimpleCommand
 * @see HistoryWorker
 */
public final class ShowCommand implements FindWorker, BaseSimpleCommand {
    public ShowCommand() {
    }

    /** The method outputs data of all registered employees. */
    @Override
    public void doExecute(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        if (findWorker(io)) {
            return;
        }
        list.stream().sorted(Comparator.comparing(Worker::getName).thenComparing(Worker::getStartDate)
                .thenComparing(Worker::getCreationDate)).map(Worker::toString).forEach(io::println);
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.SHOW;
    }
}
