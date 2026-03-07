package edu.itmo.piikt.validationModels;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRules<T> {
    Optional<String> validation(T value);
    default ValidationRules<T> rules(ValidationRules<T> other) {
        return value -> {
            Optional<String> input = this.validation(value);
            return input.isPresent() ? input : other.validation(value);
        };
    }
}
