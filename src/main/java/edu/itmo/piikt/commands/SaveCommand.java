package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.MessageCommand;
import edu.itmo.piikt.reader.CSVParser;

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
