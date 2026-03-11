package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseArgumentCommand;
import edu.itmo.piikt.massage.MessageCommand;

import java.time.LocalDate;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see BaseArgumentCommand
 * @see IOProvider
 * @see HistoryWorker
 */
public final class RemoveLowerCommand implements BaseArgumentCommand {
    public RemoveLowerCommand() {
    }
    @Override
    public void doExecute(IOProvider io, String argument) {
        LocalDate date = LocalDate.parse(argument.trim());
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        listWorker.removeIf(worker -> worker.getStartDate().isAfter(date));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.REMOVE_LOVER;
    }
}
