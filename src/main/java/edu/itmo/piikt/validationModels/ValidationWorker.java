package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.*;
import edu.itmo.piikt.reader.InputReader;

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
    private InputReader scanner;
    public ValidationWorker(IOProvider io){
        this.io = io;
        this.coordinates = new ValidationCoordinates(io);
        this.status = new ValidationStatus(io);
        this.organization = new ValidationOrganization(io);
        this.scanner = InputReader.getInstance();
    }

    public String validationName(){
        //Введите имя (поле обязательно для заполнения)
        io.printField("Enter name", "(field is required)");
        String nameConsole = scanner.nextLine();
        while(true) {
            if (!nameConsole.isBlank()) {
                return nameConsole;
                //Поле не введено, повторите попытку
            }else io.printException("Field not entered, please try again");
        }
    }

    public Coordinates validationNullCoordinates(){
        //Введите координаты (поле обязательно для заполнения)
        io.printField("Enter coordinates", "(field is required)");
        while(true) {
            Coordinates coordinatesConsole = coordinates.сoordinates();
            if (coordinatesConsole != null) {
                return coordinatesConsole;
                //Поле не введено, повторите попытку
            } else io.printException("Field not entered, please try again");
        }
    }

    public Float validationSalary(){
        while (true){
            //Введите заработную плату (значение должно быть больше 0)
            io.printField("Enter salary", "(value must be greater than 0)");
            try {
                String input = scanner.nextLine();
                    if (input.isEmpty()) {
                        return null;
                    }
                float salaryConsole = Float.parseFloat(input);
                if (salaryConsole > 0) {
                    return salaryConsole;
                    //введенное значение не положительное, повторите попытку
                }else  System.out.println("Entered value is not positive, please try again");}
            catch (RuntimeException e) {
                System.out.println("Invalid input, please enter the value again");
            }}
    }

    public Date validationStartDate(){
        while (true){
            //Введите дату начала работу (формат: 30-01-2024, поле обязательно к заполнения)
            io.printField("Enter start date", "(format: 30-01-2024, field is required)");
            String startDateConsole = scanner.nextLine();
            if (startDateConsole != null && !startDateConsole.isBlank()){
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    return format.parse(startDateConsole);
                } catch (ParseException e) {
                    System.out.println("Invalid input, please enter the value again");
                }
            }else System.out.println("Invalid input, please enter the value again");}
    }

    public ZonedDateTime validationEndDate(){
        while(true){
            //Введите дату увольнения (формат: 2026-02-15)
            io.printField("Enter dismissal date", "(format: 2026-02-15)");

            String endDateConsole = scanner.nextLine();
            if (endDateConsole == null || endDateConsole.isBlank()) {
                return null;
            }

            try{
                LocalTime timeNow = LocalTime.now();
                LocalDate date = LocalDate.parse(endDateConsole);
                ZonedDateTime dateTime = ZonedDateTime.of(date, timeNow, ZoneId.systemDefault());
                return dateTime;
            } catch (DateTimeParseException e) {
                System.out.println("The date could not be parsed into the required format");}
        }
    }

    public Status validationNullStatus(){
        System.out.println("Choose one of the statuses and write it in uppercase");
        while(true){
            //введите статус (поле обязательно к заполнению)
            io.printField("Enter status", "(field is required)");
            Status statusConsole = status.status();
            if (statusConsole != null) {
                return statusConsole;
                //поле не заполнено, повторите попытку
            }else io.printException("Field is empty, please try again");
        }
    }

    public Worker worker(){
        return  new Worker(validationName(), validationNullCoordinates(),
                validationSalary(), validationStartDate(), validationEndDate(),
                validationNullStatus(), organization.organization());
    }
}