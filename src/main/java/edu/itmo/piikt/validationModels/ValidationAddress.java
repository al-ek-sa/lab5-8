package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.reader.InputReader;

public class ValidationAddress {
    private InputReader scanner;
    public ValidationAddress(){
        this.scanner = InputReader.getInstance();
    }

    public Address validationAddress() {
            System.out.println("Enter the street");
        while (true){
            String streetConsole = scanner.nextLine();
            if (!streetConsole.isBlank()){
                return new Address(streetConsole);
            }else System.out.println("Required field");}
    }
}