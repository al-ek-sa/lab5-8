package edu.itmo.piikt.exception;

/**
 * The class for outputting the script value error. The class extends RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExceptionScript extends RuntimeException {
    public ExceptionScript() {}

    @Override
    public String getMessage() {
        //в команде началась рекурсия, исправьте скрипт и повторите снова(дфва раза встречаеться файл : .txt)
        return "Command recursion detected. Fix the script and retry (duplicate file:";
    }
}
