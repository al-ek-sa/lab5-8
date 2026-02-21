package edu.itmo.piikt.exception;

public class ExceptionBigIntegerMIN_INTEGER extends RuntimeException {
    public ExceptionBigIntegerMIN_INTEGER() {}

    @Override
    public String getMessage() {
        //Число слишком маленькое, оно меньше, повторите попытку
        return "The number is too small, it is less than " + Integer.MIN_VALUE + ", please try again.";
    }
}
