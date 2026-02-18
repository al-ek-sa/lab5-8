package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

public class ValidationStatus {
    private IOProvider io;
    private InputReader scanner;
    public ValidationStatus(IOProvider io){
        this.scanner = InputReader.getInstance();
        this.io = io;
    }

    public Status status() {
        while (true){
            System.out.println("Status options");
            for (Status status : Status.values()) {
                System.out.println("(" +status.getId() +") " + status.name());
            }
            try {
                String idStatus = scanner.nextLine();
                int id = Integer.parseInt(idStatus);
                for (Status status : Status.values()) {
                    if (status.getId() == id) {
                        return status;
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("Invalid input, please enter the value again");
            }
        }
    }
}