package edu.itmo.piikt.validation.modelValidation;

import edu.itmo.piikt.*;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.massage.ConsoleMessage;
import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.validation.builder.Builder;
import edu.itmo.piikt.validation.builder.RulesValidation;
import edu.itmo.piikt.validation.builder.TypeIOProvider;
import edu.itmo.piikt.validation.builder.Validation;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum OrganizationType.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see Function
 * @see TypeIOProvider
 * @see Validation
 * @see Builder
 * @see ConsoleMessage
 * @see IOProvider
 * @see OrganizationType
 */
public class ValidationOrganizationType implements TypeIOProvider {
    private final Function<IOProvider, OrganizationType> organizationValidation;
    public ValidationOrganizationType(IOProvider io) {
        Validation validation = type(io);

        this.organizationValidation = new Builder<BigInteger>().add(RulesValidation.integerMIN())
                .add(RulesValidation.integerMAX()).add(RulesValidation.enumRuler(OrganizationType.values().length))
                .validation(validation).build(reader -> {
                    ConsoleMessage.ENUM.printMessage(reader);
                    for (OrganizationType organizationType : OrganizationType.values()) {
                        reader.println("(" + organizationType.getId() + ") " + organizationType.name());
                    }
                    return new BigInteger(reader.readLine());
                }).andThen(input -> OrganizationType.values()[input.intValue() - 1]);
    }

    public OrganizationType organizationType(IOProvider io) {
        return organizationValidation.apply(io);
    }
}
