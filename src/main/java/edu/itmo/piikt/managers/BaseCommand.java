package edu.itmo.piikt.managers;

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
