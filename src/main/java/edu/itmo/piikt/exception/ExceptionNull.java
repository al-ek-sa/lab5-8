package edu.itmo.piikt.exception;

/**
 * The class for outputting an error if a field was not filled in or a value of 0 was entered. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionNull extends  RuntimeException{
    public ExceptionNull(){}
    @Override
    public String getMessage() {
        //поле обязательно для заполнения, повторите попытку
        return "The field is required, please try again";
    }
}
