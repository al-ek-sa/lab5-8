package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;

public class CommandFactory {
    private static final AppLogger logger = new AppLogger(CommandFactory.class);
    private final SaveCommand saveCommand = new SaveCommand();

    public void execute(String command) {
        try (Context context = Context.newId()) {
            logger.debug("Processing console command: {}", command);
            if (DamerauLevenshteinDistance.distance(command, saveCommand.getName()) <= 1) {
                logger.info("Executing SAVE command from console");
                saveCommand.execute();
            } else {
                logger.debug("Unknown console command: {}", command);
            }
        } catch (Exception e) {
            logger.error("Error executing console command: {}", e);
        }
    }
}