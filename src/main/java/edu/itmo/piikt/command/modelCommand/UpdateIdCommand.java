package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.interfaces.IdMatches;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseArgumentCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.models.Worker;
import edu.itmo.piikt.validation.modelValidation.ValidationWorker;

import java.util.UUID;

/**
 * The class implements the command update id {element} : update the value of
 * the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class UpdateIdCommand implements IdMatches, BaseArgumentCommand {
    public UpdateIdCommand() {
    }
    /**
     * The method replaces the element whose id is equal to the id specified by the
     * user.
     *
     * @throws RuntimeException
     *             If the user entered the id in an incorrect format.
     */
    @Override
    public void doExecute(IOProvider io, String argument) {
        UUID.fromString(argument);
        idMatches(argument, io);
        var workers = HistoryWorker.INSTANCE.getListWorker();
        workers.removeIf(w -> w.getId().equals(argument));
        Worker newWorker = new ValidationWorker(io).worker(io);
        HistoryWorker.INSTANCE.add(newWorker);
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.UPDATE_ID;
    }
}
