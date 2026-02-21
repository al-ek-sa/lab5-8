package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.ExceptionBigDecimalMAX_FLOAT;
import edu.itmo.piikt.exception.ExceptionDate;
import edu.itmo.piikt.exception.ExceptionNull;
import edu.itmo.piikt.exception.ExceptionSalary;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.format.DateTimeParseException;

public class ValidationWorker {
    private IOProvider io;
    private ValidationCoordinates coordinates;
    private ValidationStatus status;
    private ValidationOrganization organization;
    public ValidationWorker(IOProvider io){
        this.io = io;
        this.coordinates = new ValidationCoordinates(io);
        this.status = new ValidationStatus(io);
        this.organization = new ValidationOrganization(io);
    }

    public String validationName(){
        if (io.name().equals("File")){
            String nameConsole = io.readLine();
            if (!nameConsole.isBlank() && !nameConsole.equals("null")) {
                return nameConsole;
                //Поле не введено, повторите попытку
            }else {
                throw  new RuntimeException("Field not entered, please try again");
            }
        }

        if (io.name().equals("Console")) {
            while (true) {
                try{
                //Введите имя (поле обязательно для заполнения)
                io.printField("Enter name", "(field is required)");
                String nameConsole = io.readLine();
                if (!nameConsole.isBlank() && !nameConsole.equals("null")) {
                    return nameConsole;
                } else {
                    throw new ExceptionNull();
                }
            } catch (ExceptionNull e){
                    io.printException(e.getMessage());
                }
            }

        }else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    public Coordinates validationNullCoordinates(){
        if (io.name().equals("File")){
            Coordinates coordinatesConsole = coordinates.сoordinates();
            if (coordinatesConsole != null) {
                return coordinatesConsole;
                //Поле не введено, повторите попытку
            } else {
                throw new RuntimeException("Field not entered, please try again");
            }
        }



        if (io.name().equals("Console")) {
            while (true) {
                try {
                    //Введите координаты (поле обязательно для заполнения)
                    io.printField("Enter coordinates", "(field is required)");
                    Coordinates coordinatesConsole = coordinates.сoordinates();
                    if (coordinatesConsole != null) {
                        return coordinatesConsole;
                        //Поле не введено, повторите попытку
                    } else {
                        throw new ExceptionNull();
                    }
                }catch (ExceptionNull e) {
                    io.printException(e.getMessage());
                }
            }
        }else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    public Float validationSalary(){
        if (io.name().equals("File")){
            try {
                String input = io.readLine();
                if (input.isEmpty() || input.equals("null")) {
                    return null;
                }

                String inputFloat = input.replace(',', '.');
                float salaryConsole = Float.parseFloat(inputFloat);
                if (salaryConsole > 0) {
                    return salaryConsole;
                    //введенное значение не положительное, повторите попытку
                }else  {
                    throw new RuntimeException("Entered value is not positive, please try again");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("Invalid input, please enter the value again");
            }
        }


        if (io.name().equals("Console")) {
            while (true) {
                //Введите заработную плату (значение должно быть больше 0)
                io.printField("Enter salary", "(value must be greater than 0)");
                try {
                    String input = io.readLine();
                    if (input.isEmpty() || input.equals("null")) {
                        return null;
                    }

                    String inputFloat = input.replace(',', '.');

                    BigDecimal bigDecimal = new BigDecimal(inputFloat);

                    if (bigDecimal.compareTo(BigDecimal.valueOf(Float.MAX_VALUE)) == 1) {
                        throw new ExceptionBigDecimalMAX_FLOAT();
                    }

                    if (bigDecimal.compareTo(BigDecimal.valueOf(Float.MIN_VALUE)) == -1) {
                        throw new ExceptionSalary();
                    }

                    float salaryConsole = Float.parseFloat(inputFloat);
                    if (salaryConsole > 0) {
                        return salaryConsole;
                        //введенное значение не положительное, повторите попытку
                    } else {
                        throw new ExceptionSalary();
                    }
                }catch (ExceptionBigDecimalMAX_FLOAT e) {
                    io.printException(e.getMessage());
                }catch (ExceptionSalary e) {
                    io.printException(e.getMessage());
                } catch (RuntimeException e) {
                    io.printException("The string contains symbols, please try again");
                }
            }
        } else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    public LocalDate validationStartDate(){
        if (io.name().equals("File")){
            String startDateConsole = io.readLine();
            if (!startDateConsole.equals("null") && !startDateConsole.isBlank()){
                try {
                    LocalDate format = LocalDate.parse(startDateConsole);
                    return format;
                } catch (DateTimeParseException e) {
                    throw new RuntimeException("Invalid input, please enter the value again");
                }
            }else {
                throw new RuntimeException("Invalid input, please enter the value again");
            }
        }


        if (io.name().equals("Console")) {
            while (true) {
                try {
                    //Введите дату начала работу (формат: 30-01-2024, поле обязательно к заполнения)
                    io.printField("Enter start date", "(format: 2024-01-15, field is required)");
                    String startDateConsole = io.readLine();

                    if (startDateConsole.equals("null") || startDateConsole.isBlank()){
                        throw new ExceptionNull();
                    }

                    if (!startDateConsole.equals("null") && !startDateConsole.isBlank()) {
                        try {
                            LocalDate format = LocalDate.parse(startDateConsole);
                            return format;
                        } catch (DateTimeParseException e) {
                            io.printException("Invalid input, please enter the value again");
                        }
                    } else {
                        throw new ExceptionDate();
                    }
                }catch (ExceptionNull e){
                    io.printException(e.getMessage());
                } catch (ExceptionDate e) {
                    io.printException(e.getMessage());
                }
            }
        } else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    public ZonedDateTime validationEndDate(){
        if (io.name().equals("File")){
            String endDateConsole = io.readLine();
            if (endDateConsole.equals("null") || endDateConsole.isBlank()) {
                return null;
            }

            try{
                LocalTime timeNow = LocalTime.now();
                LocalDate date = LocalDate.parse(endDateConsole);
                return ZonedDateTime.of(date, timeNow, ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                throw new RuntimeException("The date could not be parsed into the required format");}
        }


        if (io.name().equals("Console")) {
            while (true) {

                try {
                    //Введите дату увольнения (формат: 2026-02-15)
                    io.printField("Enter dismissal date", "(format: 2026-02-15)");

                    String endDateConsole = io.readLine();

                    if (endDateConsole.equals("null") || endDateConsole.isBlank()) {
                        return null;
                    }

                    try {
                        LocalTime timeNow = LocalTime.now();
                        LocalDate date = LocalDate.parse(endDateConsole);
                        return ZonedDateTime.of(date, timeNow, ZoneId.systemDefault());
                    } catch (DateTimeParseException e) {
                        io.printException("The date could not be parsed into the required format");
                    }
                }catch (RuntimeException e) {
                    io.printException(e.getMessage());
                }
            }
        }else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    public Status validationNullStatus(){
        if (io.name().equals("File")){
            Status statusConsole = status.status();
            if (statusConsole != null) {
                return statusConsole;
                //поле не заполнено, повторите попытку
            }else{
                throw new RuntimeException("Field is empty, please try again");
            }
        }

        if (io.name().equals("Console")) {
            while (true) {
                //введите статус (поле обязательно к заполнению)
                io.printField("Enter status", "(field is required)");
                Status statusConsole = status.status();
                if (statusConsole != null) {
                    return statusConsole;
                    //поле не заполнено, повторите попытку
                } else {
                    throw new RuntimeException("Field is empty, please try again");
                }
            }
        }else{
            throw new RuntimeException("Unknown reading type");
        }
    }

    public Worker worker(){
        return  new Worker(validationName(), validationNullCoordinates(),
                validationSalary(), validationStartDate(), validationEndDate(),
                validationNullStatus(), organization.organization());
    }
}