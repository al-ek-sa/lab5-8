package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveCommand {
    private static final AppLogger logger = new AppLogger(SaveCommand.class);
    private final String name = "save";

    public void execute() {
        try (Context ignored = Context.newId()) {
            logger.info("Executing SAVE command");
            CSVParser csvParser = new CSVParser();
            csvParser.saveCollection();
            logger.info("Collection saved successfully");
        } catch (Exception e) {
            logger.error("Error executing SAVE command: {}", e);
            throw new RuntimeException(e);
        }
    }
}