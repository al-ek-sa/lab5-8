package edu.itmo.piikt.validation.builder;

import java.util.Optional;

/**
 * Validates a value and returns an error message if validation fails.
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @param <T> the type of value to validate
 * @see Optional
 */
@FunctionalInterface
public interface ValidationRules<T> {
    Optional<String> validation(T value);
}
