package edu.itmo.piikt.exception;

public class ExceptionBigIntegerMIN_LONG extends RuntimeException{

    public ExceptionBigIntegerMIN_LONG(){}

    @Override
    public String getMessage() {
        //Число слишком маленькое, оно меньше, повторите попытку
        return "The number is too small, it is less than " + Long.MIN_VALUE + ", please try again.";
    }
}
