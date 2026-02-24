package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.models.OrganizationType;

import java.math.BigInteger;

/**
 * The class generates an Organization with the specified conditions:
 *
 * <ul>
 * <li>private int annualTurnover; //Значение поля должно быть больше 0</li>
 * <li>private OrganizationType type; //Поле не может быть null</li>
 * <li>private Address officialAddress; //Поле не может быть null</li>
 * </ul>
 *
 * <p>The class provides a method that validates the field values.</p>
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ValidationOrganization {
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private IOProvider io;
    public ValidationOrganization(IOProvider io){
        this.type = new ValidationOrganizationType(io);
        this.address = new ValidationAddress(io);
        this.io = io;
    }

    /**
     *The method returns an Address object.
     *
     * @throws RuntimeException If the object value is null.
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @return Address
     */
    public Address validationNullAddress(){

        if (io.name().equals("File")){
            Address addressConsole = address.validationAddress();
            if (addressConsole != null ){
                return addressConsole;
                //поле не заполнено, повторите попытку
            } else {
                throw new RuntimeException("Field is empty, please try again");}
        }


        if(io.name().equals("Console")) {
            while (true) {
                //Введите адрес (поле обязательно к заполнению)
                io.printField("Enter the address", "(required field)");
                Address addressConsole = address.validationAddress();
                if (addressConsole != null) {
                    return addressConsole;
                    //поле не заполнено, повторите попытку
                } else {
                    throw new RuntimeException("Field is empty, please try again");
                }
            }
        }else {
            throw new RuntimeException("Field is empty, please try again");
        }
    }

    /**
     *The method checks if the OrganizationType enum instance value is null.
     *
     * @return OrganizationType
     */
    public OrganizationType validationNullOrganizationType(){
        OrganizationType organizationTypeConsole = type.organizationType();
        if (organizationTypeConsole != null ) {
            return organizationTypeConsole;
        } return validationNullOrganizationType();
    }

    /**
     *The method validates the annualTurnover value.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException If an incorrect value is entered in the file.
     * @throws ExceptionBigIntegerMAX_INTEGER If the value entered in the console exceeds the upper limit of the int type.
     * @throws ExceptionAnnualTunover If the value entered in the console is not greater than zero or falls below the lower limit of the int type range.
     * @return annualTurnover
     */
    public int validationAnnualTurnover() {

        if (io.name().equals("File")) {
            String input = io.readLine();
            int annualTurnoverConsole = Integer.parseInt(input);
            if (annualTurnoverConsole > 0) {
                return annualTurnoverConsole;
                //Введено неположительное значение, повторите попытку
            } else {
                throw new RuntimeException("A non-positive value has been entered, please try again");
            }
        }

        if (io.name().equals("Console")) {
            while (true) {
                try {
                    //Введите годовой оборот (годовой оборот должен быть целым числом больше 0, поле обязательно к заполнению)
                    io.printField("Enter annual turnover", "(annual turnover must be an integer greater than 0. Field is required)");
                    String input = io.readLine();

                    if (input.equals("null") || input.trim().isEmpty()) {
                        throw new ExceptionNull();
                    }

                    BigInteger bigInteger = new BigInteger(input);

                    if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                        throw new ExceptionBigIntegerMAX_INTEGER();
                    }

                    if (bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                        throw new ExceptionAnnualTunover();
                    }

                    int annualTurnoverConsole = Integer.parseInt(input);
                    if (annualTurnoverConsole > 0) {
                        return annualTurnoverConsole;
                        //Введено неположительное значение, повторите попытку
                    } else {
                        throw new ExceptionAnnualTunover();
                    }
                } catch (ExceptionNull e) {
                    io.printException(e.getMessage());
                } catch (ExceptionBigIntegerMAX_INTEGER e){
                    io.printException(e.getMessage());
                } catch (ExceptionBigIntegerMIN_INTEGER e) {
                    io.printException(e.getMessage());
                }catch (ExceptionAnnualTunover e) {
                    io.printException(e.getMessage());
                }
                catch (RuntimeException e) {
                    io.printException("The string contains symbols, please try again");
                }
            }
        }else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    /**
     *The method returns an Organization object with validated fields.
     *
     * @return Organization
     */
    public Organization organization(){
        return new Organization(validationAnnualTurnover(), validationNullOrganizationType(), validationNullAddress());
    }
}
