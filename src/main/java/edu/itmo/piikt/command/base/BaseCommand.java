package edu.itmo.piikt.command.base;

import edu.itmo.piikt.massage.MessageCommand;

import java.util.logging.Logger;

public interface BaseCommand {
    MessageCommand getMessageCommand();

    default Logger logger() {
        return Logger.getLogger(getClass().getName());
    }

    default void before() {
        getMessageCommand().loggerBefore(logger());
    }
    default void after() {
        getMessageCommand().loggerAfter(logger());
    }
    default void onError(RuntimeException e) {
        getMessageCommand().loggerError(logger(), e);
    }
}
