package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseArgumentCommand;
import edu.itmo.piikt.massage.MessageCommand;

import java.util.UUID;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class RemoveLowerCommand implements BaseArgumentCommand {
    public RemoveLowerCommand() {
    }
    @Override
    public void doExecute(IOProvider io, String argument) {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        UUID input = UUID.fromString(argument);
        listWorker.removeIf(worker -> {
            UUID workerUuid = UUID.fromString(worker.getId());
            return workerUuid.compareTo(input) < 0;
        });
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.REMOVE_LOVER;
    }
}
