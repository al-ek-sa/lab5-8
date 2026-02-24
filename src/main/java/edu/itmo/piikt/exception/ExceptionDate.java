package edu.itmo.piikt.exception;

/**
 * The class for outputting the date value error. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionDate extends RuntimeException {
    public ExceptionDate() {}

    @Override
    public String getMessage() {
        //формат даты не совпадает с примером, повторите попытку снова
        return "The date format does not match the example, please try again";
    }
}
