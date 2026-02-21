package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

import java.math.BigInteger;

public class ValidationOrganizationType {
    private IOProvider io;
    public ValidationOrganizationType(IOProvider io){
        this.io = io;
    }

    public OrganizationType organizationType(){
        if (io.name().equals("File")){
            try {
                String idStatus = io.readLine();
                int id = Integer.parseInt(idStatus);
                for (OrganizationType type : OrganizationType.values()) {
                    if (type.getId() == id) {
                        return type;
                    }
                }
            } catch (RuntimeException e) {
                //Либо ничего не введено либо цифры не те либо буквы
                throw  new RuntimeException("Invalid input, please enter the value again");
            }
        }


        if (io.name().equals("Console")) {
            while (true) {
                //Выберите тип организации (введите ее номер)
                io.printField("Select the organization type", "(enter its number)");
                for (OrganizationType type : OrganizationType.values()) {
                    io.println("(" + type.getId() + ") " + type.name());
                }
                try {
                    String idStatus = io.readLine();

                    if (idStatus.equals("null")|| idStatus.trim().isEmpty()) {
                        throw new ExceptionNull();
                    }

                    BigInteger bigInteger = new BigInteger(idStatus);

                    if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) == 1 || bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) == -1) {
                        throw new ExceptionEnum();
                    }

                    int id = Integer.parseInt(idStatus);

                    if (id <1 || id >5){
                        throw new ExceptionEnum();
                    }
                    for (OrganizationType type : OrganizationType.values()) {
                        if (type.getId() == id) {
                            return type;
                        }
                    }
                }catch (ExceptionNull e) {
                    io.printException(e.getMessage());
                }catch (ExceptionEnum e) {
                    io.printException(e.getMessage());
                }catch (RuntimeException e){
                    io.printException("The string contains symbols, please try again.");
                }
            }
        } else {
            throw new RuntimeException("Unknown reading type");
        }
    }
}
