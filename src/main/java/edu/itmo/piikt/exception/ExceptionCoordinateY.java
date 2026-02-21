package edu.itmo.piikt.exception;

public class ExceptionCoordinateY extends RuntimeException {
    public ExceptionCoordinateY() {}

    @Override
    public String getMessage() {
        //введенное число меньше -644, повторите попытку снова
        return "The entered number is less than -644, please try again";
    }
}
