package edu.itmo.piikt.exception;

public class ExceptionId extends RuntimeException {
    public ExceptionId() {}

    @Override
    public String getMessage() {
        //id должно быть больше 0
        return "ID must be greater than 0";
    }
}
