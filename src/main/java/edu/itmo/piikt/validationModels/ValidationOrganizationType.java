package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.OrganizationType;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum OrganizationType.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
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
