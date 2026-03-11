package edu.itmo.piikt.validation.modelValidation;

import edu.itmo.piikt.*;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.massage.ConsoleMessage;
import edu.itmo.piikt.models.Coordinates;
import edu.itmo.piikt.validation.builder.Builder;
import edu.itmo.piikt.validation.builder.RulesValidation;
import edu.itmo.piikt.validation.builder.TypeIOProvider;
import edu.itmo.piikt.validation.builder.Validation;

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
 * @see Function
 * @see TypeIOProvider
 * @see Validation
 * @see Builder
 * @see ConsoleMessage
 * @see IOProvider
 */
public class ValidationCoordinates implements TypeIOProvider {
    private final Function<IOProvider, Long> xValidation;
    private final Function<IOProvider, Float> yValidation;
    public ValidationCoordinates(IOProvider io) {
        Validation validationIO = type(io);

        this.xValidation = new Builder<BigInteger>().add(RulesValidation.longMIN()).add(RulesValidation.xCoordinate())
                .validation(validationIO).build(reader -> {
                    ConsoleMessage.X_COORDINATE.printMessage(reader);
                    return new BigInteger(reader.readLine());
                }).andThen(BigInteger::longValue);

        this.yValidation = new Builder<BigDecimal>().add(RulesValidation.floatMAX()).add(RulesValidation.yCoordinate())
                .validation(validationIO).build(reader -> {
                    ConsoleMessage.Y_COORDINATE.printMessage(reader);
                    return new BigDecimal(reader.readLine());
                }).andThen(BigDecimal::floatValue);

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
