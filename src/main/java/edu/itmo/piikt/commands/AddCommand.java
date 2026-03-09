package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.MessageCommand;
import edu.itmo.piikt.validationModels.ValidationWorker;

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
