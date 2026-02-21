package edu.itmo.piikt.exception;

public class ExceptionNull extends  RuntimeException{
    public ExceptionNull(){}
    @Override
    public String getMessage() {
        //поле обязательно для заполнения, повторите попытку
        return "The field is required, please try again";
    }
}
