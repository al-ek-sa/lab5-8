package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

public class ValidationStatus {
    private IOProvider io;
    public ValidationStatus(IOProvider io){
        this.io = io;
    }

    public Status status() {
        if (io.name().equals("File")){
            try {
                String idStatus = io.readLine();
                int id = Integer.parseInt(idStatus);
                for (Status status : Status.values()) {
                    if (status.getId() == id) {
                        return status;
                    }
                }
            } catch (RuntimeException e) {
                //тоже самое что и в типе
                throw new RuntimeException("Invalid input, please enter the value again");
            }
        }


        if (io.name().equals("Console")) {
            while (true) {
                //выберите статус (введите его номер)
                io.printField("Select the status", "(enter its number)");
                for (Status status : Status.values()) {
                    System.out.println("(" + status.getId() + ") " + status.name());
                }
                try {
                    String idStatus = io.readLine();
                    int id = Integer.parseInt(idStatus);
                    for (Status status : Status.values()) {
                        if (status.getId() == id) {
                            return status;
                        }
                    }
                } catch (RuntimeException e) {

                    //тоже самое что и в типе
                    System.out.println("Invalid input, please enter the value again");
                }
            }
        } else {
            throw new RuntimeException("ошибочка");
        }
    }
}