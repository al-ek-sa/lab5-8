package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

import java.math.BigInteger;

/**
 * The class returns the selected instance of the enum Status.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */

public class ValidationStatus {
    private IOProvider io;
    public ValidationStatus(IOProvider io){
        this.io = io;
    }

    /**
     *The method returns an instance of the enum Status based on the entered instance number.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException When the number is not entered in the file, there are errors parsing the entered value into an int,
     * or when the entered number is not found among the registered instance numbers.
     * @throws ExceptionNull If no value is entered into the console or null is entered.
     * @throws ExceptionEnum If the value entered into the console does not match the instance numbers,
     * as well as when entering values outside the range of int.
     * @throws RuntimeException When there are errors parsing the value entered into the console into an int.
     * @return Status
     */
    public Status status() {
            while (true) {
                //выберите статус (введите его номер)
                io.printField("Select the status", "(enter its number)");
                for (Status status : Status.values()) {
                    io.println("(" + status.getId() + ") " + status.name());
                }
                try {
                    String idStatus = io.readLine();

                    if (idStatus.equals("null") || idStatus.trim().isEmpty()) {
                        if (io.name().equals("File")) {
                            throw new RuntimeException("_______________________________");
                        } else {
                            throw new ExceptionNull();
                        }
                    }

                    BigInteger bigInteger = new BigInteger(idStatus);

                    if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 || bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) <0) {
                        if (io.name().equals("File")) {
                            throw new RuntimeException("__________________________________");
                        } else {
                            throw new ExceptionEnum();
                        }
                    }
                    int id = Integer.parseInt(idStatus);

                    if (id < 1 || id > Status.values().length){
                        if (io.name().equals("File")) {
                            throw new RuntimeException("________________________________");
                        } else {
                            throw new ExceptionEnum();
                        }
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
                    if (io.name().equals("File")) {
                        throw new RuntimeException("___________________________________");
                    } else {
                        io.printException("The string contains symbols, please try again.");
                    }

            }
        }
    }
}