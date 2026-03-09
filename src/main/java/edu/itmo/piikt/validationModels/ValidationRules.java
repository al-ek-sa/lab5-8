package edu.itmo.piikt.validationModels;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRules<T> {
    Optional<String> validation(T value);
}
