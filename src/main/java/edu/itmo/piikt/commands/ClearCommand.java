package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.Confirmation;
import edu.itmo.piikt.managers.MessageCommand;

/**
 * The class implements the command clear : clear the collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
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
        io.printlnCommand("Are you sure you want to clear the collection? (yes/no)");
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.CLEAR;
    }

    @Override
    public void refusal(IOProvider io) {
        io.println("Consent received, clearing collection");
    }
}
