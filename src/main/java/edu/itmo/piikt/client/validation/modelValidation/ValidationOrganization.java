package edu.itmo.piikt.client.validation.modelValidation;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.message.ConsoleMessage;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.client.validation.builder.Builder;
import edu.itmo.piikt.client.validation.builder.RulesValidation;
import edu.itmo.piikt.client.validation.builder.TypeIOProvider;
import edu.itmo.piikt.client.validation.builder.Validation;

import java.math.BigInteger;
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
 * @see TypeIOProvider
 * @see Validation
 * @see Builder
 * @see ConsoleMessage
 * @see IOProvider
 */
public class ValidationOrganization implements TypeIOProvider {
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private final Function<IOProvider, Integer> annualTurnoverValidation;

    public ValidationOrganization(IOProvider io) {
        this.type = new ValidationOrganizationType(io);
        this.address = new ValidationAddress(io);
        Validation validationIO = type(io);

        this.annualTurnoverValidation = new Builder<BigInteger>().add(RulesValidation.integerMAX())
                .add(RulesValidation.annualTurnover()).validation(validationIO).build(reader -> {
                    ConsoleMessage.ANNUAL_TURNOVER.printMessage(reader);
                    return new BigInteger(reader.readLine());
                }).andThen(BigInteger::intValue);
    }

    public int validationAnnualTurnover(IOProvider io) {
        return annualTurnoverValidation.apply(io);
    }

    /**
     * The method returns an Organization object with validated fields.
     *
     * @return Organization
     */
    public Organization organization(IOProvider io) {
        return new Organization(validationAnnualTurnover(io), type.organizationType(io), address.validationAddress(io));
    }
}
