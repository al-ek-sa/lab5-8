package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.validation.modelValidation.ValidationWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command add {element} : add a new element to the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see BaseSimpleCommand
 * @see IOProvider
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class AddCommand implements BaseSimpleCommand {
    @Override
    public void doExecute(IOProvider io) {
        ValidationWorker worker = new ValidationWorker(io);
        HistoryWorker.INSTANCE.add(worker.worker(io));
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.ADD;
    }
}
