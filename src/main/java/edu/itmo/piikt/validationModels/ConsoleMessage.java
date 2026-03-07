package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;

public enum ConsoleMessage {
    STREET("Enter the first coordinate X", "value must not exceed 10, required field");
    private final String messageOne;
    private final String messageTwo;

    ConsoleMessage(String messageOne, String messageTwo) {
        this.messageOne = messageOne;
        this.messageTwo = messageTwo;
    }

    public void printMessage(IOProvider io) {
        io.printField(messageOne, messageTwo);
    }
}
