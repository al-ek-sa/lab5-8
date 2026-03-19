package edu.itmo.piikt.client.validation.builder;

import edu.itmo.piikt.client.validation.exception.ValidationException;
import edu.itmo.piikt.client.io.provider.IOProvider;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeParseException;
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
 * @see Validation
 * @see IOProvider
 */
@NoArgsConstructor
public class Builder<T> {
    private final List<ValidationRules<T>> rulesList = new ArrayList<>();
    private Validation validation;

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
     * Sets the validation mode.
     *
     * @param valid
     *            the validation mode
     * @return this builder for method chaining
     */
    public Builder<T> validation(Validation valid) {
        this.validation = valid;
        return this;
    }

    /**
     * Builds a validation function based on the configured rules and mode.
     * @param ioProvider
     * @return a function that takes IOProvider and returns a validated value
     */
    //todo убрать дублирование и вынести проверки
    public Function<IOProvider, T> build(Function<IOProvider, T> ioProvider) {
        if (!validation.isRepeat()) {
            return (IOProvider reader) -> {
                try {
                    T value = ioProvider.apply(reader);
                    Optional<String> exception = rulesList.stream()
                            .map(rule -> rule.validation(value))
                            .filter(Optional::isPresent)
                            .findFirst()
                            .flatMap(Function.identity());
                    if (exception.isPresent()) {
                        throw new ValidationException(exception.get());
                    }
                    return value;
                } catch (RuntimeException e) {
                    throw new ValidationException("Validation error: " + e.getMessage());
                }
            };
        }

        return (IOProvider reader) -> {
            while (true) {
                try {
                    T value = ioProvider.apply(reader);
                    Optional<String> exception = rulesList.stream()
                            .map(rule -> rule.validation(value))
                            .filter(Optional::isPresent)
                            .findFirst()
                            .flatMap(Function.identity());
                    if (exception.isPresent()) {
                        throw new ValidationException(exception.get());
                    }
                    return value;
                } catch (ValidationException e) {
                    validation.getMessageError().accept(reader, e.getMessage());
                } catch (RuntimeException e) {
                    String error = switch (e) {
                        case NullPointerException npe -> "null";
                        case DateTimeParseException dtpe -> "parser";
                        case NumberFormatException nfe -> "The string contains symbols, please try again";
                        default -> "Validation error:" + e.getMessage();
                    };
                    validation.getMessageError().accept(reader, error);
                }
            }
        };
    }
}
