package edu.itmo.piikt.exception;

/**
 * The class for outputting errors if the variable value exceeds Integer_MAX. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionBigIntegerMAX_INTEGER extends RuntimeException {
    public ExceptionBigIntegerMAX_INTEGER() {}

    @Override
    public String getMessage() {
        //Число слишком большое, оно превышает, повторите попытку
        return "The number is too large, it exceeds " + Integer.MAX_VALUE+ ", please try again.";
    }
}
