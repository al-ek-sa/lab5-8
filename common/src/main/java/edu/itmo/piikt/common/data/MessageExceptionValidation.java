package edu.itmo.piikt.common.data;

/**
 * Record for validation error messages
 *
 * @param name
 *            name of the field that failed validation
 * @param message
 *            error description
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record MessageExceptionValidation(String name, String message) {
}
