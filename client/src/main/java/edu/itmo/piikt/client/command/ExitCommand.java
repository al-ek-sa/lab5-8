package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import lombok.NoArgsConstructor;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 4.1
 */
@NoArgsConstructor
public final class ExitCommand {
    private static final AppLogger logger = new AppLogger(ExitCommand.class);

    /**
     * Executes the exit command, stopping the client application.
     * Sets the validation flag to false, which breaks the main command loop.
     */
    public void execute() {
        try (Context ignored = Context.newId()) {
            logger.info("Exit command received, shutting down client");
            ValidationCommand.INSTANCE.setFlag(false);
            logger.info("Client shutdown initiated");
        } catch (Exception e) {
            logger.error("Error during exit: {}", e);
        }
    }
}