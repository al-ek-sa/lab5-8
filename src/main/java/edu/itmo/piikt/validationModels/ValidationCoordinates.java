package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Coordinates;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

/**
 * The class generates Coordinates with the specified fields:
 *
 * <ul>
 * <li>private long x; //Maximum field value: 10
 * <li>private float y; //The field value must be greater than -644
 * </ul>
 *
 * <p>
 * The class provides methods that validate the field values.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class ValidationCoordinates implements TypeIOProvider {
    private final Function<IOProvider, Long> xValidation;
    private final Function<IOProvider, Float> yValidation;
    public ValidationCoordinates(IOProvider io) {
        Validation validationIO = type(io);

        this.xValidation = new Builder<BigInteger>().add(RulesValidation.longMAX()).add(RulesValidation.longMIN())
                .validation(validationIO).build(reader -> {
                    ConsoleMessage.X_COORDINATE.printMessage(reader);
                    return new BigInteger(reader.readLine());
                }).andThen(input -> {
                    long x = input.longValue();
                    if (x > 10)
                        throw new ValidationException(ValidationMessage.COORDINATE_X.getText());
                    return x;
                });

        this.yValidation = new Builder<BigDecimal>().add(RulesValidation.floatMAX()).validation(validationIO)
                .build(reader -> {
                    ConsoleMessage.Y_COORDINATE.printMessage(reader);
                    return new BigDecimal(reader.readLine());
                }).andThen(input -> {
                    float y = input.floatValue();
                    if (y <= -644)
                        throw new ValidationException(ValidationMessage.COORDINATE_Y.getText());
                    return y;
                });

    }
    public Long validatorX(IOProvider io) {
        return xValidation.apply(io);
    }

    public Float validatorY(IOProvider io) {
        return yValidation.apply(io);
    }

    /**
     * The method returns a Coordinates object with validated fields.
     *
     * @return Coordinates
     */
    public Coordinates coordinates(IOProvider io) {
        return new Coordinates(validatorX(io), validatorY(io));
    }
}
