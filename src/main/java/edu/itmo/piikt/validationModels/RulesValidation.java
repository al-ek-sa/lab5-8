package edu.itmo.piikt.validationModels;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public final class RulesValidation {
    private RulesValidation() {
    }

    public static ValidationRules<Long> xCoordinate() {
        return x -> x > 10 ? Optional.of(ValidationMessage.COORDINATE_X.getText()) : Optional.empty();
    }

    public static ValidationRules<Float> yCoordinate() {
        return y -> y <= -644 ? Optional.of(ValidationMessage.COORDINATE_Y.getText()) : Optional.empty();
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

    public static ValidationRules<BigDecimal> floatMIN() {
        return max -> max.compareTo(BigDecimal.valueOf(Float.MIN_VALUE)) < 0
                ? Optional.of(ValidationMessage.MIN_FLOAT.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> longMAX() {
        return max -> max.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                ? Optional.of(ValidationMessage.MAX_LONG.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> longMIN() {
        return min -> min.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0
                ? Optional.of(ValidationMessage.MIN_LONG.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> integerMAX() {
        return max -> max.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                ? Optional.of(ValidationMessage.MAX_INTEGER.getText())
                : Optional.empty();
    }

    public static ValidationRules<BigInteger> integerMIN() {
        return min -> min.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0
                ? Optional.of(ValidationMessage.MIN_INTEGER.getText())
                : Optional.empty();
    }

    public static ValidationRules<Integer> enumRuler(int max) {
        return line -> (line < 1 || line > max) ? Optional.of(ValidationMessage.ENUM.getText()) : Optional.empty();
    }
}
