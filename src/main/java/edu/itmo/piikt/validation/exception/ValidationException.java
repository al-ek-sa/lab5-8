package edu.itmo.piikt.validation.exception;

/**Exception thrown when validation fails.
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see RuntimeException
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

}
