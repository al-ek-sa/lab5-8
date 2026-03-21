package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.NoArgsConstructor;

/**
 * The class implements the command save : save the collection to a file.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see IOProvider
 * @see BaseSimpleCommand
 */
@NoArgsConstructor
public final class SaveCommand implements BaseSimpleCommand {
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
