package edu.itmo.piikt.server.validation.builder;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Builds a validation function based on the configured rules and mode.
 *
 * @param <T>
 *            the type of value being validated
 * @author Lishyk Aliaksandra
 * @version 1.1
 * @see ValidationRules
 * @see Function
 */
@AllArgsConstructor
public class Builder<T> {
    private static final AppLogger logger = new AppLogger(Builder.class);
    private final List<ValidationRules<T>> rulesList = new ArrayList<>();
    private final String name;

    /**
     * Adds a validation rule to the chain.
     *
     * @param rules
     *            the validation rule to add
     * @return this builder for method chaining
     */
    public Builder<T> add(ValidationRules<T> rules) {
        rulesList.add(rules);
        return this;
    }

    /**
     * Builds a validation function based on the configured rules and mode.
     *
     * @return a function that takes IOProvider and returns a validated value
     */
    public Function<T, Optional<MessageExceptionValidation>> build() {
        return input -> {
            try (Context context = Context.newId()) {
                for (ValidationRules<T> rule : rulesList) {
                    Optional<String> error = rule.validation(input);
                    if (error.isPresent()) {
                        logger.debug("Validation failed for field '{}': {}", name, error.get());
                        return Optional.of(new MessageExceptionValidation(name, error.get()));
                    }
                }
                logger.debug("Validation passed for field '{}'", name);
                return Optional.empty();
            } catch (Exception e) {
                logger.error("Error validating field '{}': {}", name, e.getMessage());
                return Optional.of(new MessageExceptionValidation(name, "Validation error: " + e.getMessage()));
            }
        };
    }
}