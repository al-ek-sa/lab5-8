package edu.itmo.piikt.server.command.exception;

/**
 * The class for outputting the script value error. The class extends
 * RuntimeException.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ExceptionScript extends RuntimeException {
    public ExceptionScript() {
    }

    @Override
    public String getMessage() {
        return "Command recursion detected. Fix the script and retry (duplicate file:";
    }
}
