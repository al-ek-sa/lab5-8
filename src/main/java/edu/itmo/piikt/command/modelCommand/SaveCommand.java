package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.saveManager.CSVParser;

/**
 * The class implements the command save : save the collection to a file.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class SaveCommand implements BaseSimpleCommand {
    public SaveCommand() {
    }
    @Override
    public void doExecute(IOProvider io) {
        CSVParser csvParser = new CSVParser();
        csvParser.saveCollection();
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.SAVE;
    }
}
