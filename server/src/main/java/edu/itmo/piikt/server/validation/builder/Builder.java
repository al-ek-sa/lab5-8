package edu.itmo.piikt.server.validation.builder;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
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
 * @version 1.0
 * @see ValidationRules
 * @see Function
 * @see IOProvider
 */
@AllArgsConstructor
public class Builder<T> {
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
            for (ValidationRules<T> rule : rulesList) {
                Optional<String> error = rule.validation(input);
                if (error.isPresent()) {
                    return Optional.of(new MessageExceptionValidation(name, error.get()));
                }
            }
            return Optional.empty();
        };
    }
}
