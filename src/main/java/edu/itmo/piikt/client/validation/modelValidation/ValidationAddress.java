package edu.itmo.piikt.client.validation.modelValidation;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.message.ConsoleMessage;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.client.validation.builder.Builder;
import edu.itmo.piikt.client.validation.builder.RulesValidation;
import edu.itmo.piikt.client.validation.builder.TypeIOProvider;
import edu.itmo.piikt.client.validation.builder.Validation;

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
 * @see TypeIOProvider
 * @see Validation
 * @see Builder
 * @see ConsoleMessage
 * @see IOProvider
 */
public class ValidationAddress implements TypeIOProvider {
    private final Function<IOProvider, String> addressValidation;
    public ValidationAddress(IOProvider io) {
        Validation validationIO = type(io);

        this.addressValidation = new Builder<String>().add(RulesValidation.blank()).validation(validationIO)
                .build(reader -> {
                    ConsoleMessage.STREET.printMessage(reader);
                    return reader.readLine();
                });
    }

    /**
     * The method validates the address.
     *
     * @return Address
     */
    public Address validationAddress(IOProvider io) {
        String street = addressValidation.apply(io);
        return new Address(street);
    }
}
