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
            io.print("Enter the street where the employee lives ");
            io.println("(required field)");
            String streetConsole = io.readLine();
            if (!streetConsole.isBlank()){
                return new Address(streetConsole);
            }else io.printException("Field is empty, please try again");}
    }
}