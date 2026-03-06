package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Organization;
import java.math.BigInteger;

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
 */
public class ValidationOrganization {
    private ValidationOrganizationType type;
    private ValidationAddress address;

    public ValidationOrganization() {
        this.type = new ValidationOrganizationType();
        this.address = new ValidationAddress();
    }

    /**
     * The method validates the annualTurnover value.
     *
     * @throws RuntimeException
     *             The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException
     *             If an incorrect value is entered in the file.
     * @throws ExceptionBigIntegerMAX_INTEGER
     *             If the value entered in the console exceeds the upper limit of
     *             the int type.
     * @throws ExceptionAnnualTunover
     *             If the value entered in the console is not greater than zero or
     *             falls below the lower limit of the int type range.
     * @return annualTurnover
     */
    public int validationAnnualTurnover(IOProvider io) {
        while (true) {
            try {
                io.printField("Enter annual turnover",
                        "(annual turnover must be an integer greater than 0. Field is required)");
                String input = io.readLine();
                if (input.equals("null") || input.trim().isEmpty()) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionNull();
                    }
                }
                BigInteger bigInteger = new BigInteger(input);
                if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionBigIntegerMAX_INTEGER();
                    }
                }
                if (bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionAnnualTunover();
                    }
                }
                int annualTurnoverConsole = Integer.parseInt(input);
                if (annualTurnoverConsole > 0) {
                    return annualTurnoverConsole;
                } else {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionAnnualTunover();
                    }
                }
            } catch (ExceptionNull e) {
                io.printException(e.getMessage());
            } catch (ExceptionBigIntegerMAX_INTEGER e) {
                io.printException(e.getMessage());
            } catch (ExceptionBigIntegerMIN_INTEGER e) {
                io.printException(e.getMessage());
            } catch (ExceptionAnnualTunover e) {
                io.printException(e.getMessage());
            } catch (RuntimeException e) {
                if (io.name().equals("File")) {
                    throw new RuntimeException();
                } else {
                    io.printException("The string contains symbols, please try again");
                }
            }
        }
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
