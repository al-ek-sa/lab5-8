package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.massage.MessageConfirmation;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.interfaces.confirmation.Confirmation;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see Confirmation
 * @see BaseSimpleCommand
 * @see IOProvider
 * @see HistoryWorker
 * @see MessageConfirmation
 */
public final class ClearCommand implements Confirmation, BaseSimpleCommand {
    public ClearCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        Boolean consent = confirmation(io);
        if (consent == true) {
            HistoryWorker.INSTANCE.clear();
        }
    }

    @Override
    public void question(IOProvider io) {
        io.printlnCommand(MessageConfirmation.CLEAR.getQuestion());
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.CLEAR;
    }

    @Override
    public void refusal(IOProvider io) {
        io.println(MessageConfirmation.CLEAR.getRefusal());
    }
}
