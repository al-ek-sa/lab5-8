package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveCommand {
    private final String name = "save";
    public void execute() {
        CSVParser csvParser = new CSVParser();
        csvParser.saveCollection();
    }
}
