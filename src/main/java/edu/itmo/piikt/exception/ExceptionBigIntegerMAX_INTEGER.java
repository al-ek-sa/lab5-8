package edu.itmo.piikt.exception;

public class ExceptionBigIntegerMAX_INTEGER extends RuntimeException {
    public ExceptionBigIntegerMAX_INTEGER() {}

    @Override
    public String getMessage() {
        //Число слишком большое, оно превышает, повторите попытку
        return "The number is too large, it exceeds " + Integer.MAX_VALUE+ ", please try again.";
    }
}
