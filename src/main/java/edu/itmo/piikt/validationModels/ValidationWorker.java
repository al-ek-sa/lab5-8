package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.*;
import edu.itmo.piikt.reader.InputReader;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.format.DateTimeParseException;

public class ValidationWorker {
    private ValidationCoordinates coordinates;
    private ValidationStatus status;
    private ValidationOrganization organization;
    private InputReader scanner;
    public ValidationWorker(){
        this.coordinates = new ValidationCoordinates();
        this.status = new ValidationStatus();
        this.organization = new ValidationOrganization();
        this.scanner = InputReader.getInstance();
    }

    public String validationName(){
        String nameConsole = scanner.nextLine();
        if(!nameConsole.isBlank()) {
            return  nameConsole;
        } return validationName();
    }

    public Coordinates validationNullCoordinates(){
        if (coordinates.validationCoordinates() != null) {
            return coordinates.validationCoordinates();
        } return validationNullCoordinates();
    }

    public Float validationSalary(){
        Float salaryConsole = scanner.nextFloat();
        if (salaryConsole == null || salaryConsole > 0) {
            return salaryConsole;
        } return  validationSalary();
    }

    public Date validationStartDate(){
        String startDateConsole = scanner.nextLine();
        if (startDateConsole != null && !startDateConsole.isBlank()){
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                return format.parse(startDateConsole);
            } catch (ParseException e) {
                System.out.println();
                return validationStartDate();
            }}return  validationStartDate();
    }

    public ZonedDateTime validationEndDate(){
        String endDateConsole = scanner.nextLine();

        if (endDateConsole == null || endDateConsole.isBlank()) {
            return null;
        }

        try{
            return ZonedDateTime.parse(endDateConsole);
        } catch (DateTimeParseException e) {
            return validationEndDate();
        }
    }

    public Status validationNullStatus(){
        if (status.validationStatus() != null) {
            return status.validationStatus();
        }
        return validationNullStatus();
    }

    public Worker validationWorker(){
        return  new Worker(validationName(), validationNullCoordinates(),
                validationSalary(), validationStartDate(), validationEndDate(),
                validationNullStatus(), organization.validationOrganization());
    }
}