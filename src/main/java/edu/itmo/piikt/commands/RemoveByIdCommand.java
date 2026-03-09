package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseArgumentCommand;
import edu.itmo.piikt.managers.MessageCommand;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class RemoveByIdCommand implements IdMatches, BaseArgumentCommand {
    public RemoveByIdCommand() {
    }
    @Override
    public void doExecute(IOProvider io, String argument) {
        idMatches(argument, io);
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        listWorker.removeIf(worker -> worker.getId().equals(argument));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.REMOVE_BY_ID;
    }
}
