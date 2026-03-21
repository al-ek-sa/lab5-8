package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class generates an Organization with the specified conditions:
 *
 * <ul>
 * <li>private int annualTurnover; //Значение поля должно быть больше 0
 * <li>private OrganizationType type; //Поле не может быть null
 * <li>private Address officialAddress; //Поле не может быть null
 * </ul>
 *
 * <p>
 * The class provides a method that validates the field values.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 * @see Function
 * @see Builder
 */
public class ValidationOrganization {
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private final Function<BigInteger, Optional<MessageExceptionValidation>> annualTurnoverValidation;

    public ValidationOrganization() {
        this.type = new ValidationOrganizationType();
        this.address = new ValidationAddress();

        this.annualTurnoverValidation = new Builder<BigInteger>("annual turnover").add(RulesValidation.integerMAX())
                .add(RulesValidation.annualTurnover()).build();
    }

    public Optional<MessageExceptionValidation> validationAnnualTurnover(Integer annualTurnover) {
        return annualTurnoverValidation.apply(BigInteger.valueOf(annualTurnover));
    }

    /**
     * The method returns an Organization object with validated fields.
     *
     * @return Organization
     */
    public Organization organization(Integer annualTurnover, String street, Integer typeId) {
        return new Organization(validationAnnualTurnover(annualTurnover), type.organizationType(typeId), address.validationAddress(street));
    }
}
