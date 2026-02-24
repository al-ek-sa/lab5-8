package edu.itmo.piikt.exception;

import edu.itmo.piikt.io.IOProvider;

/**
 * The class for outputting the X value error. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionCoordinataX extends RuntimeException{
    public ExceptionCoordinataX(){}

    @Override
    public String getMessage() {
        //Введенное значение некорректно. Х не должно превышать 10
        return "Invalid coordinate. X must not exceed 10";
    }
}
