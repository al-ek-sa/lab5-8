package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

public class ValidationStatus {
    private InputReader scanner;
    public ValidationStatus(){
        this.scanner = InputReader.getInstance();
    }

    public Status validationStatus(){
        System.out.println("Выберите один из статусов и запишите в верхнем регистре");
        for (Status status : Status.values()){
            System.out.println(status);
        }
        String statusConsole = scanner.nextLine();

        for (Status nameStatus : Status.values()){
            if (nameStatus.name().equals(statusConsole)){
                return nameStatus;
            }
        } return validationStatus();
    }
}