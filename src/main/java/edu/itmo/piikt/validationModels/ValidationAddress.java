package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;

public class ValidationAddress {
    private IOProvider io;
    public ValidationAddress(IOProvider io){
        this.io = io;
    }

    public Address validationAddress() {
        while (true){
            //Введите улицу, на которой проживает сотрудник (поле обязательно для заполнения)
            io.printField("Enter the street where the employee lives", "(required field)");
            String streetConsole = io.readLine();
            if (!streetConsole.isBlank()){
                return new Address(streetConsole);
            }else io.printException("Field is empty, please try again");} //поле не заполнено, повторите попытку
    }
}