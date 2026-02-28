package edu.itmo.piikt.exception;

/**
 * The class for outputting the id value error. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionId extends RuntimeException {
    public ExceptionId() {}

    @Override
    public String getMessage() {
        //id должно быть больше 0
        return "ID must be greater than 0";
    }
}
