package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;

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
 * @version 2.1
 * @see Function
 * @see Builder
 */
public class ValidationOrganization {
    private static final AppLogger logger = new AppLogger(ValidationOrganization.class);
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private final Function<String, Optional<MessageExceptionValidation>> annualTurnoverValidation;

    public ValidationOrganization() {
        this.type = new ValidationOrganizationType();
        this.address = new ValidationAddress();
        this.annualTurnoverValidation = new Builder<String>("annual turnover")
                .add(RulesValidation.validationAnnualTurnover()).build();
        logger.debug("ValidationOrganization initialized");
    }

    public Optional<MessageExceptionValidation> validationAnnualTurnover(String annualTurnover) {
        try (Context context = Context.newId()) {
            logger.debug("Validating annual turnover: {}", annualTurnover);
            return annualTurnoverValidation.apply(annualTurnover);
        } catch (Exception e) {
            logger.error("Error validating annual turnover: {}", e.getMessage());
            return Optional.of(new MessageExceptionValidation("annual turnover", "Validation error: " + e.getMessage()));
        }
    }
}