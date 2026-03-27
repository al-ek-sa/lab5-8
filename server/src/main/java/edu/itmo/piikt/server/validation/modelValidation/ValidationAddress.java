package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Function;

/**
 * The class generates an address with the specified conditions:
 *
 * <ul>
 * <li>private String street; //The field cannot be null
 * </ul>
 *
 * <p>
 * The class provides a method that validates the field values.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see Function
 * @see Builder
 */
@Getter
public class ValidationAddress {
    private static final AppLogger logger = new AppLogger(ValidationAddress.class);
    private final Function<String, Optional<MessageExceptionValidation>> addressValidation;

    public ValidationAddress() {
        this.addressValidation = new Builder<String>("street").add(RulesValidation.blank()).build();
        logger.debug("ValidationAddress initialized");
    }

    public Optional<MessageExceptionValidation> validation(String street) {
        try (Context context = Context.newId()) {
            logger.debug("Validating street: {}", street);
            return addressValidation.apply(street);
        } catch (Exception e) {
            logger.error("Error validating street: {}", e.getMessage());
            return Optional.of(new MessageExceptionValidation("street", "Validation error: " + e.getMessage()));
        }
    }
}