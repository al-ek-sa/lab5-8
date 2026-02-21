package edu.itmo.piikt.exception;

public class ExceptionAnnualTunover extends RuntimeException {
    public ExceptionAnnualTunover() {}

    @Override
    public String getMessage() {
        return "A non-positive value has been entered, please try again";
    }
}
