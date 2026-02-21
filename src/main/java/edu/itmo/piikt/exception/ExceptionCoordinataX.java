package edu.itmo.piikt.exception;

import edu.itmo.piikt.io.IOProvider;

public class ExceptionCoordinataX extends RuntimeException{
    public ExceptionCoordinataX(){}

    @Override
    public String getMessage() {
        //Введенное значение некорректно. Х не должно превышать 10
        return "Invalid coordinate. X must not exceed 10";
    }
}
