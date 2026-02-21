package edu.itmo.piikt.exception;

public class ExceptionSalary extends RuntimeException {
    public ExceptionSalary() {}

    @Override
    public String getMessage() {
        //введенное значение не больше 0, повторите попытку
        return "The entered value is not greater than 0, please try again";
    }
}
