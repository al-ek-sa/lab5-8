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

/**
 *The class generates an employee with the specified conditions:
 * <ul>
 * <li>private int id; //The field value must be greater than 0, The value of this field must be unique, The value of this field must be generated automatically</li>
 * <li>private String name; //The field cannot be null, The string cannot be empty</li>
 * <li>private Coordinates coordinates; //The field cannot be null</li>
 * <li>private java.util.Date creationDate; //The field cannot be null, The value of this field must be generated automatically</li>
 * <li>private Float salary; //The field can be null, The field value must be greater than 0</li>
 * <li>private java.time.LocalDate startDate; //The field cannot be null</li>
 * <li>private java.time.ZonedDateTime endDate; //The field can be null</li>
 * <li>private Status status; //The field cannot be null</li>
 * <li>private Organization organization; //The field can be null</li>
 * </ul>
 *  <p>The class provides methods that validate the field values.</p>
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
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

    /**The method validates the name value.
     *The method has two implementations: reading from the console and reading from a file.
     * If a field is missing in the file, the method returns nothing.
     * If a field is missing in the console, the method prompts for re-entry.
     *
     * @throws ExceptionNull When reading from the console, if a field is not filled in
     * @throws RuntimeException When reading a script, if the name is not entered.
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @return name
     */
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

    /**
     *The method checks if the coordinates object is null.
     *
     * @throws RuntimeException When reading a file, if the object is null
     * @throws ExceptionNull When reading from the console, if the object is null
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @return Coordinates
     */
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


    /**
     *The method validates the salary value.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException When the value is not greater than 0.
     * @throws RuntimeException When there is an error parsing the entered value into Float.
     * @throws ExceptionBigDecimalMAX_FLOAT When a value outside the range of Float is entered into the console.
     * @throws ExceptionSalary When a value not greater than 0 is entered into the console
     * @throws RuntimeException Errors when parsing the value entered into the console is incorrect for the Float type.
     * @return salary
     */
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

    /**
     *The method validates the startDate values.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException When the value in the file is in an incorrect format or not entered.
     * @throws ExceptionNull When no value is entered into the console.
     * @throws ExceptionDate When the input format of the value in the console is incorrect.
     * @return startDate
     */

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


    /**
     *The method validates endDate.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws DateTimeParseException When the date is entered incorrectly or not entered.
     * @return endDate
     */
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


    /**
     * The method checks if the Status object is null.
     *
     * @throws RuntimeException When reading a file, if the object is null
     * @throws RuntimeException When reading from the console, if the object is null
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @return Status
     */
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

    /**
     * The method creates an employee considering all validations.
     * @return Worker
     */
    public Worker worker(){
        return  new Worker(validationName(), validationNullCoordinates(),
                validationSalary(), validationStartDate(), validationEndDate(),
                validationNullStatus(), organization.organization());
    }
}