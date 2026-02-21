package edu.itmo.piikt.exception;

public class ExceptionBigIntegerMAX_LONG extends RuntimeException {
    public ExceptionBigIntegerMAX_LONG(){}

    @Override
    public String getMessage() {
        //Число слишком большое, оно превышает, повторите попытку
        return "The number is too large, it exceeds " + Long.MAX_VALUE+ ", please try again.";
    }
}
