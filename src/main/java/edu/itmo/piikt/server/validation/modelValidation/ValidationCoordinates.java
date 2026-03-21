package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
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
 * @see Builder
 */
public class ValidationCoordinates {
    private final Function<BigInteger, Optional<MessageExceptionValidation>> xValidation;
    private final Function<BigDecimal, Optional<MessageExceptionValidation>> yValidation;
    public ValidationCoordinates() {

        this.xValidation = new Builder<BigInteger>("x").add(RulesValidation.longMIN()).add(RulesValidation.xCoordinate()).build();

        this.yValidation = new Builder<BigDecimal>("y").add(RulesValidation.floatMAX()).add(RulesValidation.yCoordinate()).build();

    }
    public Optional<MessageExceptionValidation> validationX(long x) {
        return xValidation.apply(BigInteger.valueOf(x));
    }

    public Optional<MessageExceptionValidation> validationY(float y) {
        return yValidation.apply(BigDecimal.valueOf(y));
    }

    /**
     * The method returns a Coordinates object with validated fields.
     *
     * @return Coordinates
     */
    //todo исключение может бросить
    public Coordinates coordinates(long x, float y) {
        return new Coordinates(x, y);
    }
}
