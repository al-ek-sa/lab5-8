package edu.itmo.piikt.exception;

/**
 * The class for outputting errors if the variable value exceeds Float_MAX. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionBigDecimalMAX_FLOAT extends RuntimeException {
    public ExceptionBigDecimalMAX_FLOAT() {}

    @Override
    public String getMessage() {
        //Число слишком большое, оно превышает, повторите попытку
        return "The number is too large, it exceeds " + Float.MAX_VALUE+ ", please try again.";
    }
}
