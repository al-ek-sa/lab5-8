package edu.itmo.piikt.massage;

/**
 * Enum with a set of messages that will be displayed to the user when the form
 * is filled out incorrectly.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum ValidationMessage {
    ANNUAL_TURNOVER("A non-positive value has been entered, please try again"),

    SALARY("The entered value is not greater than 0, please try again"),

    DATE("The date format does not match the example, please try again"),

    COORDINATE_X("Invalid coordinate. X must not exceed 10"),

    COORDINATE_Y("The entered number is less than -644, please try again"),

    MAX_FLOAT("The number is too large, it exceeds " + Float.MAX_VALUE + ", please try again."),

    MAX_INTEGER("The number is too large, it exceeds " + Integer.MAX_VALUE + ", please try again."),

    MAX_LONG("The number is too large, it exceeds " + Long.MAX_VALUE + ", please try again."),

    MIN_INTEGER("The number is too small, it is less than " + Integer.MIN_VALUE + ", please try again."),

    MIN_LONG("The number is too small, it is less than " + Long.MIN_VALUE + ", please try again."),

    MIN_FLOAT("The number is too small, it is less than " + Float.MIN_VALUE + ", please try again."),

    NULL("The field is required, please try again"),

    ENUM("The entered value does not match the numbers of the suggested options");

    private final String text;
    ValidationMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
