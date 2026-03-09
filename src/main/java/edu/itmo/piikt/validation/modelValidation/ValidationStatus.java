package edu.itmo.piikt.validation.modelValidation;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.massage.ConsoleMessage;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.validation.builder.Builder;
import edu.itmo.piikt.validation.builder.RulesValidation;
import edu.itmo.piikt.validation.builder.TypeIOProvider;
import edu.itmo.piikt.validation.builder.Validation;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum Status.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class ValidationStatus implements TypeIOProvider {
    private final Function<IOProvider, Status> statusValidation;
    public ValidationStatus(IOProvider io) {
        Validation validation = type(io);

        this.statusValidation = new Builder<BigInteger>().add(RulesValidation.integerMAX())
                .add(RulesValidation.integerMIN()).add(RulesValidation.enumRuler(Status.values().length))
                .validation(validation).build(reader -> {
                    ConsoleMessage.ENUM.printMessage(reader);
                    for (Status status : Status.values()) {
                        reader.println("(" + status.getId() + ") " + status.name());
                    }

                    return new BigInteger(reader.readLine());
                }).andThen(input -> Status.values()[input.intValue() - 1]);
    }

    public Status status(IOProvider io) {
        return statusValidation.apply(io);
    }
}
