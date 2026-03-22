package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
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
    private final Function<String, Optional<MessageExceptionValidation>> organizationValidation;
    public ValidationOrganizationType() {
        this.organizationValidation = new Builder<String>("organization type").add(RulesValidation.validationType()).build();}

    public Optional<MessageExceptionValidation> validationOrganizationType(String type) {
        return organizationValidation.apply(type);
    }

    //todo nullpointer оч аккуратно
    /**public OrganizationType organizationType(int type) {
        return OrganizationType.values()[type -1];
    }*/
}
