package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum OrganizationType.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 * @see Function
 * @see Builder
 * @see OrganizationType
 */
public class ValidationOrganizationType {
    private final Function<BigInteger, Optional<MessageExceptionValidation>> organizationValidation;
    public ValidationOrganizationType() {
        this.organizationValidation = new Builder<BigInteger>("organization type").add(RulesValidation.integerMIN())
                .add(RulesValidation.integerMAX()).add(RulesValidation.enumRuler(OrganizationType.values().length))
                .build();}

    public Optional<MessageExceptionValidation> validationOrganizationType(Integer type) {
        return organizationValidation.apply(BigInteger.valueOf(type));
    }

    public OrganizationType organizationType(int type) {
        return OrganizationType.values()[type -1];
    }
}
