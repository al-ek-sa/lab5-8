package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
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
    private final Function<String, Optional<MessageExceptionValidation>> xValidation;
    private final Function<String, Optional<MessageExceptionValidation>> yValidation;
    public ValidationCoordinates() {

        this.xValidation = new Builder<String>("x").add(RulesValidation.validationX2()).build();

        this.yValidation = new Builder<String>("y").add(RulesValidation.validationY2()).build();

    }
    public Optional<MessageExceptionValidation> validationX(String x) {
        return xValidation.apply(x);
    }

    public Optional<MessageExceptionValidation> validationY(String y) {
        return yValidation.apply(y);
    }
}
