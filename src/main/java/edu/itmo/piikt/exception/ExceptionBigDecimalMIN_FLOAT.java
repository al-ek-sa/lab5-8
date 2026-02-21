package edu.itmo.piikt.exception;

public class ExceptionBigDecimalMIN_FLOAT extends RuntimeException {
    public ExceptionBigDecimalMIN_FLOAT() {}

    @Override
    public String getMessage() {
        //Число слишком маленькое, оно меньше, повторите попытку
        return "The number is too small, it is less than " + Float.MIN_VALUE + ", please try again.";
    }
}
