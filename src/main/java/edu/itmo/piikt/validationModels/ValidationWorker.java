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
        System.out.println("Enter name");
        String nameConsole = scanner.nextLine();
        while(true) {
            if (!nameConsole.isBlank()) {
                return nameConsole;
            }else System.out.println("Invalid name, please try again");
            System.out.println("Enter name");
        }
    }

    public Coordinates validationNullCoordinates(){
        io.print("Enter coordinates");
        while(true) {
            Coordinates coordinatesConsole = coordinates.сoordinates();
            if (coordinatesConsole != null) {
                return coordinatesConsole;
            } else io.printException("Invalid coordinates, please try again");
        }
    }

    public Float validationSalary(){
        while (true){
            io.println("Enter salary (value must be greater than 0)");
            try {
                String input = scanner.nextLine();
                    if (input.isEmpty()) {
                        return null;
                    }
                float salaryConsole = Float.parseFloat(input);
                if (salaryConsole > 0) {
                    return salaryConsole;
                }else  System.out.println("Invalid input, please enter the value again");}
            catch (RuntimeException e) {
                System.out.println("Invalid input, please enter the value again");
            }}
    }

    public Date validationStartDate(){
        while (true){
            System.out.println("Please enter the start date (format: dd-MM-yyyy)");
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
        System.out.println("Enter the end date\nInput example: 2026-02-15");
        while(true){
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
                System.out.println("The date could not be parsed into the required format");
                System.out.println("Enter the end date\nInput example: 2026-02-15");}
        }
    }

    public Status validationNullStatus(){
        System.out.println("Choose one of the statuses and write it in uppercase");
        while(true){
        Status statusConsole = status.status();
        if (statusConsole != null) {
            return statusConsole;
        }else System.out.println("Required field");
        System.out.println("Choose one of the statuses and write it in uppercase");
        }
    }

    public Worker worker(){
        return  new Worker(validationName(), validationNullCoordinates(),
                validationSalary(), validationStartDate(), validationEndDate(),
                validationNullStatus(), organization.organization());
    }
}