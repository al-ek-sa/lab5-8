package edu.itmo.piikt.exception;

/**
 * The class for outputting the Y value error. The class extends
 * RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ExceptionCoordinateY extends RuntimeException {
    public ExceptionCoordinateY() {
    }

    @Override
    public String getMessage() {
        return "The entered number is less than -644, please try again";
    }
}
