package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

import java.math.BigInteger;

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
                    io.println("(" + status.getId() + ") " + status.name());
                }
                try {
                    String idStatus = io.readLine();

                    if (idStatus.equals("null") || idStatus.trim().isEmpty()) {
                        throw new ExceptionNull();
                    }

                    BigInteger bigInteger = new BigInteger(idStatus);

                    if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) == 1 || bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) == -1) {
                        throw new ExceptionEnum();
                    }
                    int id = Integer.parseInt(idStatus);

                    if (id <1 || id >4){
                        throw new ExceptionEnum();
                    }

                    for (Status status : Status.values()) {
                        if (status.getId() == id) {
                            return status;
                        }
                    }
                }catch (ExceptionNull e) {
                    io.printException(e.getMessage());
                }catch (ExceptionEnum e) {
                    io.printException(e.getMessage());
                } catch (RuntimeException e){
                    io.printException("The string contains symbols, please try again.");
                }
            }
        } else {
            throw new RuntimeException("Unknown reading type");
        }
    }
}