package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.ExceptionNull;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;

/**
 * The class generates an address with the specified conditions:
 *
 * <ul>
 * <li>private String street; //The field cannot be null</li>
 * </ul>
 *
 * <p>The class provides a method that validates the field values.</p>
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ValidationAddress {
    private IOProvider io;
    public ValidationAddress(IOProvider io){
        this.io = io;
    }

    /**
     *The method validates the address.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException When the value is not entered in the file.
     * @throws ExceptionNull When no value is entered into the console.
     * @return Address
     */
    public Address validationAddress() {

        if (io.name().equals("File")) {
            String streetConsole = io.readLine();
            if (!streetConsole.isBlank()){
                return new Address(streetConsole);
            }else {
                throw new RuntimeException("Field 'street' is empty in file");}
        }

        if(io.name().equals("Console")) {
            while (true){
                try{
                    //Введите улицу, на которой проживает сотрудник (поле обязательно для заполнения)
                    io.printField("Enter the street where the employee lives", "(required field)");
                    String streetConsole = io.readLine();
                    if (!streetConsole.equals("null") && !streetConsole.isBlank()){
                        return new Address(streetConsole);
                    }else {throw new ExceptionNull();}
                }catch (ExceptionNull e){
                    io.printException(e.getMessage());
                }
            }
                //поле не заполнено, повторите попытку
        } else {
                //неизвестный тип чтения
                throw new RuntimeException("Unknown reading type");}
    }
}