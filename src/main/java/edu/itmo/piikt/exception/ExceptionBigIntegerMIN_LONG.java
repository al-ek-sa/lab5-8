package edu.itmo.piikt.exception;

/**
 * The class for outputting errors if the variable value is less than Long_MIN. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionBigIntegerMIN_LONG extends RuntimeException{

    public ExceptionBigIntegerMIN_LONG(){}

    @Override
    public String getMessage() {
        //Число слишком маленькое, оно меньше, повторите попытку
        return "The number is too small, it is less than " + Long.MIN_VALUE + ", please try again.";
    }
}
