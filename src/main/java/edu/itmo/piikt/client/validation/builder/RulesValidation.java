package edu.itmo.piikt.client.validation.builder;

import edu.itmo.piikt.client.message.ValidationMessage;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * A class that stores all possible rules used when adding employees.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see ValidationRules
 * @see BigDecimal
 * @see BigInteger
 * @see Number
 * @see String
 * @see Optional
 * @see ValidationMessage
 */
@NoArgsConstructor
public final class RulesValidation {

    public static ValidationRules<BigInteger> xCoordinate() {
        return x -> x.compareTo(BigInteger.valueOf(10)) > 0
                ? Optional.of(ValidationMessage.COORDINATE_X.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigDecimal> yCoordinate() {
        return y -> y.compareTo(BigDecimal.valueOf(-644)) <= 0
                ? Optional.of(ValidationMessage.COORDINATE_Y.getText())
                : Optional.empty();
    }

    public static <T extends Number> ValidationRules<T> nullPointer() {
        return line -> line == null ? Optional.of(ValidationMessage.NULL.getText()) : Optional.empty();
    }

    public static ValidationRules<String> blank() {
        return line -> line == null || line.isBlank() || "null".equalsIgnoreCase(line.trim())
                ? Optional.of(ValidationMessage.NULL.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigDecimal> floatMAX() {
        return max -> max.compareTo(BigDecimal.valueOf(Float.MAX_VALUE)) > 0
                ? Optional.of(ValidationMessage.MAX_FLOAT.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> longMIN() {
        return min -> min.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0
                ? Optional.of(ValidationMessage.MIN_LONG.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> integerMAX() {
        return max -> max.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0
                ? Optional.of(ValidationMessage.MAX_INTEGER.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> integerMIN() {
        return min -> min.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0
                ? Optional.of(ValidationMessage.MIN_INTEGER.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> enumRuler(int max) {
        return line -> {
            if (line.compareTo(BigInteger.valueOf(1)) < 0 || line.compareTo(BigInteger.valueOf(max)) > 0) {
                return Optional.of(ValidationMessage.ENUM.getText());
            }
            return Optional.empty();
        };
    }
    public static ValidationRules<BigInteger> annualTurnover() {
        return input -> input.compareTo(BigInteger.ZERO) <= 0
                ? Optional.of(ValidationMessage.ANNUAL_TURNOVER.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigDecimal> salary() {
        return input -> input.compareTo(BigDecimal.ZERO) <= 0
                ? Optional.of(ValidationMessage.ANNUAL_TURNOVER.getText())
                : Optional.empty();
    }

    public static ValidationRules<String> localDate() {
        return input -> {
            try {
                LocalDate.parse(input);
                return Optional.empty();
            } catch (DateTimeParseException e) {
                return Optional.of(ValidationMessage.DATE.getText());
            }
        };
    }

    public static ValidationRules<String> validationDate() {
        return input -> {
            if (input == null || input.isBlank() || "null".equalsIgnoreCase(input.trim())) {
                return Optional.empty();
            }
            try {
                LocalDate.parse(input);
                return Optional.empty();
            } catch (DateTimeParseException e) {
                return Optional.of(ValidationMessage.DATE.getText());
            }
        };
    }
}
