package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.server.interfaces.IdMatches;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseArgumentCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.client.validation.modelValidation.ValidationWorker;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The class implements the command update id {element} : update the value of
 * the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see IdMatches
 * @see BaseArgumentCommand
 * @see IOProvider
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class UpdateIdCommand implements IdMatches, BaseArgumentCommand {
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
        workers.removeIf(worker -> worker.getUuid().equals(argument));
        Worker newWorker = new ValidationWorker(io).worker(io);
        HistoryWorker.INSTANCE.add(newWorker);
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.UPDATE_ID;
    }
}
