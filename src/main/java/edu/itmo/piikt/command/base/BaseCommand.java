package edu.itmo.piikt.command.base;

import edu.itmo.piikt.massage.MessageCommand;

import java.util.logging.Logger;
/**
 * Specifies the execution behavior for commands.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see Logger
 * @see MessageCommand
 */
public interface BaseCommand {
    /**
     * This abstract method provides access to the {@link MessageCommand} instance,
     * which contains predefined messages for logging before, after, and during
     * error handling of the command execution.
     *
     * @return the {@link MessageCommand}
     */
    MessageCommand getMessageCommand();

    /**
     * Provides a logger instance for the current class.
     *
     * @return {@link Logger} instance
     */
    default Logger logger() {
        return Logger.getLogger(getClass().getName());
    }

    /** Logs the command execution before it starts. */
    default void before() {
        getMessageCommand().loggerBefore(logger());
    }

    /** Logs the command execution after successful completion. */
    default void after() {
        getMessageCommand().loggerAfter(logger());
    }

    /** Logs exceptions that occurred during command execution. */
    default void onError(RuntimeException e) {
        getMessageCommand().loggerError(logger(), e);
    }
}
