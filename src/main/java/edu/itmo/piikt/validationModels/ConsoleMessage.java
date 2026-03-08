package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;

public enum ConsoleMessage {
    STREET("Enter the street where the employee lives", "(required field)"),

    X_COORDINATE("Enter the first coordinate X", "(value must not exceed 10, required field)"),

    Y_COORDINATE("Enter the second coordinate Y", "(value must be greater than -644, required field)"),

    ANNUAL_TURNOVER("Enter annual turnover", "(annual turnover must be an integer greater than 0. Field is required)"),

    NAME("Enter name", "(field is required)"),

    SALARY("Enter salary", "(value must be greater than 0)"),

    START_DATE("Enter start date", "(format: 2024-01-15, field is required)"),

    END_DATE("Enter dismissal date", "(format: 2026-02-15)"),

    ENUM("Select the status", "(enter its number)"),;
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
