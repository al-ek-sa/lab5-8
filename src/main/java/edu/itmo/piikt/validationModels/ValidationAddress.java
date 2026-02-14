package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.reader.InputReader;

public class ValidationAddress {
    private InputReader scanner;
    public ValidationAddress(){
        this.scanner = InputReader.getInstance();
    }

    public Address validationAddress() {
        System.out.println("Введите улицу");
        String streetConsole = scanner.nextLine();
        if (streetConsole != null){
            return new Address(streetConsole);
        }
        System.out.println("Неверный формат, введите еще раз");
        return validationAddress();
    }
}