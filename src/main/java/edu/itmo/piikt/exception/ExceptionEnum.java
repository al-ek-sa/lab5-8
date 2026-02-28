package edu.itmo.piikt.exception;

/**
 * The class for outputting an error when selecting an Enum instance value. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionEnum extends RuntimeException {
    public ExceptionEnum() {}

    @Override
    public String getMessage() {
        //Введенное значение не совпадает с номерами предложенных вариантов
        return "The entered value does not match the numbers of the suggested options";
    }
}
