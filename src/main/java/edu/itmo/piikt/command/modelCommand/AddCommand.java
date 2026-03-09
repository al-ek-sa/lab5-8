package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.validation.modelValidation.ValidationWorker;

/**
 * The class implements the command add {element} : add a new element to the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */

// public class AddCommand implements Command {
public final class AddCommand implements BaseSimpleCommand {
    public AddCommand() {
    }
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
