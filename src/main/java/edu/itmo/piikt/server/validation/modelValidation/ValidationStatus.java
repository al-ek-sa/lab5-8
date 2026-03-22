package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.Status;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum Status.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 * @see Function
 * @see Builder
 * @see Status
 */
public class ValidationStatus {
    private final Function<BigInteger, Optional<MessageExceptionValidation>> statusValidation;
    public ValidationStatus() {

        this.statusValidation = new Builder<BigInteger>("status").add(RulesValidation.integerMAX())
                .add(RulesValidation.integerMIN()).add(RulesValidation.enumRuler(Status.values().length))
                .build();

    }

    public Optional<MessageExceptionValidation> validationStatus(Integer status) {
        return statusValidation.apply(BigInteger.valueOf(status));
    }

    public Status status(int status) {
        return Status.values()[status - 1];
    }
}
