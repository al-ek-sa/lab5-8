package edu.itmo.piikt.exception;

/**
 * The class for outputting the annualTurnover value error. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionAnnualTunover extends RuntimeException {
    public ExceptionAnnualTunover() {}

    @Override
    public String getMessage() {
        return "A non-positive value has been entered, please try again";
    }
}
