package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.OrganizationType;
import java.math.BigInteger;

/**
 * The class returns the selected instance of the enum OrganizationType.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ValidationOrganizationType {
    public ValidationOrganizationType() {
    }

    /**
     * The method returns an instance of the enum OrganizationType based on the
     * entered instance number.
     *
     * @throws RuntimeException
     *             The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException
     *             When the number is not entered in the file, there are errors
     *             parsing the entered value into an int, or when the entered number
     *             is not found among the registered instance numbers.
     * @throws ExceptionNull
     *             If no value is entered into the console or null is entered.
     * @throws ExceptionEnum
     *             If the value entered into the console does not match the instance
     *             numbers, as well as when entering values outside the range of
     *             int.
     * @throws RuntimeException
     *             When there are errors parsing the value entered into the console
     *             into an int.
     * @return OrganizationType
     */
    public OrganizationType organizationType(IOProvider io) {
        while (true) {
            io.printField("Select the organization type", "(enter its number)");
            for (OrganizationType type : OrganizationType.values()) {
                io.println("(" + type.getId() + ") " + type.name());
            }
            try {
                String idStatus = io.readLine();
                if (idStatus.equals("null") || idStatus.trim().isEmpty()) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionNull();
                    }
                }
                BigInteger bigInteger = new BigInteger(idStatus);
                if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0
                        || bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionEnum();
                    }
                }
                int id = Integer.parseInt(idStatus);
                if (id < 1 || id > OrganizationType.values().length) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionEnum();
                    }
                }
                for (OrganizationType type : OrganizationType.values()) {
                    if (type.getId() == id) {
                        return type;
                    }
                }
            } catch (ExceptionNull e) {
                io.printException(e.getMessage());
            } catch (ExceptionEnum e) {
                io.printException(e.getMessage());
            } catch (RuntimeException e) {
                if (io.name().equals("File")) {
                    throw new RuntimeException();
                } else {
                    io.printException("The string contains symbols, please try again.");
                }
            }
        }
    }
}
