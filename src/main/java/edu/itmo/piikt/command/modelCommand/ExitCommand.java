package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.interfaces.confirmation.MessageConfirmation;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.interfaces.confirmation.Confirmation;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.manager.ValidationCommand;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see IOProvider
 * @see Confirmation
 * @see BaseSimpleCommand
 * @see ValidationCommand
 * @see MessageConfirmation
 */
public final class ExitCommand implements Confirmation, BaseSimpleCommand {

    public ExitCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        Boolean consent = confirmation(io);
        if (consent == true) {
            ValidationCommand.INSTANCE.setFlag(false);
        }
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.EXIT;
    }

    @Override
    public void question(IOProvider io) {
        io.printlnCommand(MessageConfirmation.EXIT.getQuestion());
    }

    @Override
    public void refusal(IOProvider io) {
        io.println(MessageConfirmation.EXIT.getRefusal());
    }
}
