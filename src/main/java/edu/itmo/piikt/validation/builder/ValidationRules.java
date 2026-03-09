package edu.itmo.piikt.validation.builder;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRules<T> {
    Optional<String> validation(T value);
}
