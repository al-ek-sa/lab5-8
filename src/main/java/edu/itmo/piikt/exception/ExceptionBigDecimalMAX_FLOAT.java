package edu.itmo.piikt.exception;

public class ExceptionBigDecimalMAX_FLOAT extends RuntimeException {
    public ExceptionBigDecimalMAX_FLOAT() {}

    @Override
    public String getMessage() {
        //Число слишком большое, оно превышает, повторите попытку
        return "The number is too large, it exceeds " + Float.MAX_VALUE+ ", please try again.";
    }
}
