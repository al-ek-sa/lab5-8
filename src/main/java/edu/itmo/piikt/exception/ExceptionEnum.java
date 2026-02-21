package edu.itmo.piikt.exception;

public class ExceptionEnum extends RuntimeException {
    public ExceptionEnum() {}

    @Override
    public String getMessage() {
        //Введенное значение не совпадает с номерами предложенных вариантов
        return "The entered value does not match the numbers of the suggested options";
    }
}
