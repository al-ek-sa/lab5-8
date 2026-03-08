package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;

import java.util.function.BiConsumer;

public enum Validation {
    CONSOLE(true, (io, message) -> io.printException(message)), FILE(false, (io, message) -> {
    });
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
