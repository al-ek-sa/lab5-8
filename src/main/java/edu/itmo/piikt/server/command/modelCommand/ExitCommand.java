package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.massage.MessageConfirmation;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.common.interfaces.confirmation.Confirmation;
import edu.itmo.piikt.common.massage.MessageCommand;
import edu.itmo.piikt.client.manager.ValidationCommand;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public final class ExitCommand implements Confirmation, BaseSimpleCommand {
    // todo выставление флага и прокидывание ошибки break
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
