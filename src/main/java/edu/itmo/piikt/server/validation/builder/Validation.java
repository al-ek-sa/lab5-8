package edu.itmo.piikt.server.validation.builder;

import edu.itmo.piikt.client.provider.IOProvider;

import java.util.function.BiConsumer;

/**
 * Enum representing validation modes for input processing.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum Validation {
    CONSOLE(true, (io, message) -> io.printException(message)),

    FILE(false, (io, message) -> io.printException(message));
    private boolean repeat;
    private BiConsumer<IOProvider, String> messageError;

    Validation(boolean repeat, BiConsumer<IOProvider, String> messageError) {
        this.repeat = repeat;
        this.messageError = messageError;
    }

    public BiConsumer<IOProvider, String> getMessageError() {
        return messageError;
    }

    public boolean isRepeat() {
        return repeat;
    }
}
