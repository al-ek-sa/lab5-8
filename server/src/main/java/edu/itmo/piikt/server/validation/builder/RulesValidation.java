package edu.itmo.piikt.server.validation.builder;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.message.ValidationMessage;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.common.models.Status;
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
 * @version 1.1
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
    private static final AppLogger logger = new AppLogger(RulesValidation.class);

    public static ValidationRules<String> blank() {
        return line -> {
            try (Context ignored = Context.newId()) {
                boolean isInvalid = line == null || line.isBlank() || "null".equalsIgnoreCase(line.trim());
                if (isInvalid) {
                    logger.debug("Blank validation failed");
                    return Optional.of(ValidationMessage.NULL.getText());
                }
                return Optional.empty();
            }
        };
    }

    public static ValidationRules<String> localDate() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    LocalDate.parse(input);
                    return Optional.empty();
                } catch (DateTimeParseException e) {
                    logger.debug("Date validation failed: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    public static ValidationRules<String> validationDate() {
        return input -> {
            try (Context context = Context.newId()) {
                if (input == null || input.isBlank() || "null".equalsIgnoreCase(input.trim())) {
                    return Optional.empty();
                }
                try {
                    LocalDate.parse(input);
                    return Optional.empty();
                } catch (DateTimeParseException e) {
                    logger.debug("Optional date validation failed: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    // todo вынести парсинг
    public static ValidationRules<String> validationAnnualTurnover() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    Integer annualTurnover = Integer.parseInt(input);
                    if (annualTurnover <= 0) {
                        logger.debug("Annual turnover validation failed: {}", annualTurnover);
                        return Optional.of(ValidationMessage.ANNUAL_TURNOVER.getText());
                    }
                    return Optional.empty();
                } catch (NumberFormatException e) {
                    logger.debug("Annual turnover number format error: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    public static ValidationRules<String> validationY2() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    Float y = Float.parseFloat(input);
                    if (y <= -644) {
                        logger.debug("Coordinate Y validation failed: {}", y);
                        return Optional.of(ValidationMessage.COORDINATE_Y.getText());
                    }
                    return Optional.empty();
                } catch (NumberFormatException e) {
                    logger.debug("Coordinate Y number format error: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    public static ValidationRules<String> validationX2() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    long x = Long.parseLong(input);
                    if (x > 10) {
                        logger.debug("Coordinate X validation failed: {}", x);
                        return Optional.of(ValidationMessage.COORDINATE_X.getText());
                    }
                    return Optional.empty();
                } catch (NumberFormatException e) {
                    logger.debug("Coordinate X number format error: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    public static ValidationRules<String> validationType() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    Integer type = Integer.parseInt(input);
                    if (type < 1 || type > OrganizationType.values().length) {
                        logger.debug("Organization type validation failed: {}", type);
                        return Optional.of(ValidationMessage.ENUM.getText());
                    }
                    return Optional.empty();
                } catch (NumberFormatException e) {
                    logger.debug("Organization type number format error: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    public static ValidationRules<String> validationStatus() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    Integer status = Integer.parseInt(input);
                    if (status < 1 || status > Status.values().length) {
                        logger.debug("Status validation failed: {}", status);
                        return Optional.of(ValidationMessage.ENUM.getText());
                    }
                    return Optional.empty();
                } catch (NumberFormatException e) {
                    logger.debug("Status number format error: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }

    public static ValidationRules<String> validationSalary() {
        return input -> {
            try (Context context = Context.newId()) {
                try {
                    if (input == null || input.isBlank()) {
                        return Optional.empty();
                    }
                    Integer salary = Integer.parseInt(input);
                    if (salary <= 0) {
                        logger.debug("Salary validation failed: {}", salary);
                        return Optional.of(ValidationMessage.SALARY.getText());
                    }
                    return Optional.empty();
                } catch (NumberFormatException e) {
                    logger.debug("Salary number format error: {}", input);
                    return Optional.of(ValidationMessage.DATE.getText());
                }
            }
        };
    }
}