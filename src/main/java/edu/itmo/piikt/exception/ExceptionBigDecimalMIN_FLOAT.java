package edu.itmo.piikt.exception;

/**
 * The class for outputting errors if the variable value is less than Float_MIN. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionBigDecimalMIN_FLOAT extends RuntimeException {
    public ExceptionBigDecimalMIN_FLOAT() {}

    @Override
    public String getMessage() {
        //Число слишком маленькое, оно меньше, повторите попытку
        return "The number is too small, it is less than " + Float.MIN_VALUE + ", please try again.";
    }
}
