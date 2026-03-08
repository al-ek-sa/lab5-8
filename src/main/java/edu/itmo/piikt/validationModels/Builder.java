package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.ValidationException;
import edu.itmo.piikt.io.IOProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Builder<T> {
    private final List<ValidationRules<T>> rulesList = new ArrayList<>();
    private Validation validation;
    public Builder() {
    }

    public Builder<T> add(ValidationRules<T> rules) {
        rulesList.add(rules);
        return this;
    }

    public Builder<T> validation(Validation valid) {
        this.validation = valid;
        return this;
    }

    public Function<IOProvider, T> build(Function<IOProvider, T> ioProvider) {
        return (IOProvider reader) -> {
            while (validation.isRepeat()) {
                try {
                    T value = ioProvider.apply(reader);
                    Optional<String> exception = rulesList.stream().map(rule -> rule.validation(value))
                            .filter(Optional::isPresent).findFirst().flatMap(Function.identity());
                    if (exception.isPresent()) {
                        throw new ValidationException(exception.get());
                    }
                    return value;
                } catch (ValidationException e) {
                    validation.getMessageError().accept(reader, e.getMessage());
                }
            }
            throw new ValidationException("-----------------");
        };
    }
}
