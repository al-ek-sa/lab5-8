package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseArgumentCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public final class RemoveLowerCommand implements BaseArgumentCommand {
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
