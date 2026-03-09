package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.Confirmation;
import edu.itmo.piikt.managers.MessageCommand;
import edu.itmo.piikt.managers.ValidationCommand;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
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
        io.printlnCommand("Are you sure you want to exit? (yes/no)");
    }

    @Override
    public void refusal(IOProvider io) {
        io.println("Command cancelled");
    }
}
