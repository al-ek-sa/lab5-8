package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseArgumentCommand;
import edu.itmo.piikt.massage.MessageCommand;
import lombok.NoArgsConstructor;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 * @see IdMatches
 * @see BaseArgumentCommand
 * @see IOProvider
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class RemoveByIdCommand implements IdMatches, BaseArgumentCommand {
    @Override
    public void doExecute(IOProvider io, String argument) {
        idMatches(argument, io);
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        listWorker.removeIf(worker -> worker.getUuid().equals(argument));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.REMOVE_BY_ID;
    }
}
