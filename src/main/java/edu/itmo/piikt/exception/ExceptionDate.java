package edu.itmo.piikt.exception;

public class ExceptionDate extends RuntimeException {
    public ExceptionDate() {}

    @Override
    public String getMessage() {
        //формат даты не совпадает с примером, повторите попытку снова
        return "The date format does not match the example, please try again";
    }
}
