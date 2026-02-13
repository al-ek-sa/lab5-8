package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

public class ValidationStatus {
    private InputReader scanner;
    public ValidationStatus(){
        this.scanner = InputReader.getInstance();
    }

    public Status validationStatus(){
        String statusConsole = scanner.nextLine();

        for (Status nameStatus : Status.values()){
            if (nameStatus.name().equals(statusConsole)){
                return nameStatus;
            }
        } return validationStatus();
    }
}