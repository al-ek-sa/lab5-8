package edu.itmo.piikt.exception;

/**
 * The class for outputting errors if the variable value exceeds Long_MAX. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionBigIntegerMAX_LONG extends RuntimeException {
    public ExceptionBigIntegerMAX_LONG(){}

    @Override
    public String getMessage() {
        //Число слишком большое, оно превышает, повторите попытку
        return "The number is too large, it exceeds " + Long.MAX_VALUE+ ", please try again.";
    }
}
