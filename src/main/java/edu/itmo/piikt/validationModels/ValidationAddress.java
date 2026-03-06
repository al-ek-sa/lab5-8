package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.ExceptionNull;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;

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
 * @version 2.0
 */
public class ValidationAddress {
    public ValidationAddress() {
    }

    /**
     * The method validates the address.
     *
     * @throws RuntimeException
     *             The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException
     *             When the value is not entered in the file.
     * @throws ExceptionNull
     *             When no value is entered into the console.
     * @return Address
     */
    public Address validationAddress(IOProvider io) {
        while (true) {
            try {
                io.printField("Enter the street where the employee lives", "(required field)");
                String streetConsole = io.readLine();
                if (!streetConsole.equals("null") && !streetConsole.isBlank()) {
                    return new Address(streetConsole);
                } else {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("Field 'street' is empty in file");
                    } else {
                        throw new ExceptionNull();
                    }
                }
            } catch (ExceptionNull e) {
                io.printException(e.getMessage());
            }
        }
    }
}
