package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.massage.MessageConfirmation;
import edu.itmo.piikt.client.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.client.confirmation.Confirmation;
import lombok.NoArgsConstructor;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see Confirmation
 * @see BaseSimpleCommand
 * @see IOProvider
 * @see HistoryWorker
 * @see MessageConfirmation
 */
@NoArgsConstructor
public final class ClearCommand implements Confirmation{
    public void execute(IOProvider io) {
        Boolean consent = confirmation(io);
        if (consent == true) {
            HistoryWorker.INSTANCE.clear();
        }
    }

/**    @Override
    public void question(IOProvider io) {
        io.printlnCommand(MessageConfirmation.CLEAR.getQuestion());
    }*/
/**
    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.CLEAR;
    }*/
/**
    @Override
    public void refusal(IOProvider io) {
        io.println(MessageConfirmation.CLEAR.getRefusal());
    }*/
}
