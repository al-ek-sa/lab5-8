package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;

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
 * @version 3.0
 * @see Function
 * @see Builder
 */
public class ValidationAddress {
    private final Function<String, Optional<MessageExceptionValidation>> addressValidation;
    public ValidationAddress() {
//todo вынести в отдельный класс получение названия поле и кэшировать (ConcurrentHashMap)
        this.addressValidation = new Builder<String>("street").add(RulesValidation.blank())
                .build();
    }

    public Optional<MessageExceptionValidation> validation(String street) {
        return addressValidation.apply(street);
    }
}
