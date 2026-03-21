package edu.itmo.piikt.server.command.modelCommand;
import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.NoArgsConstructor;

/**
 * The class implements the command save : save the collection to a file.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 */
@NoArgsConstructor
public final class SaveCommand{
    public void execute() {
        CSVParser csvParser = new CSVParser();
        csvParser.saveCollection();
    }
}
