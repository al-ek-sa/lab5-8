package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.function.Function;

/**
 * The class generates an employee with the specified conditions:
 *
 * <ul>
 * <li>private int id; //The field value must be greater than 0, The value of
 * this field must be unique, The value of this field must be generated
 * automatically
 * <li>private String name; //The field cannot be null, The string cannot be
 * empty
 * <li>private Coordinates coordinates; //The field cannot be null
 * <li>private java.util.Date creationDate; //The field cannot be null, The
 * value of this field must be generated automatically
 * <li>private Float salary; //The field can be null, The field value must be
 * greater than 0
 * <li>private java.time.LocalDate startDate; //The field cannot be null
 * <li>private java.time.ZonedDateTime endDate; //The field can be null
 * <li>private Status status; //The field cannot be null
 * <li>private Organization organization; //The field can be null
 * </ul>
 *
 * <p>
 * The class provides methods that validate the field values.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ValidationWorker implements TypeIOProvider {
    private ValidationCoordinates coordinates;
    private ValidationStatus status;
    private ValidationOrganization organization;
    private final Function<IOProvider, String> nameValidation;
    private final Function<IOProvider, Float> salaryValidation;
    private final Function<IOProvider, LocalDate> startDateValidation;
    private final Function<IOProvider, ZonedDateTime> endDateValidation;

    public ValidationWorker(IOProvider io) {
        this.coordinates = new ValidationCoordinates(io);
        this.status = new ValidationStatus(io);
        this.organization = new ValidationOrganization(io);
        Validation validationIO = type(io);

        this.startDateValidation = new Builder<String>().add(RulesValidation.blank()).add(RulesValidation.localDate())
                .validation(validationIO).build(reader -> {
                    ConsoleMessage.START_DATE.printMessage(reader);
                    return reader.readLine();
                }).andThen(LocalDate::parse);

        this.endDateValidation = new Builder<String>().add(RulesValidation.validationDate()).validation(validationIO)
                .build(reader -> {
                    ConsoleMessage.END_DATE.printMessage(reader);
                    return reader.readLine();
                }).andThen(input -> {
                    if (input == null || input.isBlank() || "null".equalsIgnoreCase(input.trim())) {
                        return null;
                    }
                    LocalDate date = LocalDate.parse(input);
                    return ZonedDateTime.of(date, LocalTime.now(), ZoneId.systemDefault());
                });

        this.nameValidation = new Builder<String>().add(RulesValidation.blank()).validation(validationIO)
                .build(reader -> {
                    ConsoleMessage.NAME.printMessage(reader);
                    return reader.readLine();
                });

        this.salaryValidation = new Builder<BigDecimal>().add(RulesValidation.floatMAX()).add(RulesValidation.salary())
                .validation(validationIO).build(reader -> {
                    ConsoleMessage.SALARY.printMessage(reader);
                    return new BigDecimal(reader.readLine());
                }).andThen(BigDecimal::floatValue);
    }

    public String validationName(IOProvider io) {
        return nameValidation.apply(io);
    }

    public Float validationSalary(IOProvider io) {
        return salaryValidation.apply(io);
    }

    public LocalDate validationStartDate(IOProvider io) {
        return startDateValidation.apply(io);
    }

    public ZonedDateTime validationEndDate(IOProvider io) {
        return endDateValidation.apply(io);
    }

    /**
     * The method creates an employee considering all validations.
     *
     * @return Worker
     */
    public Worker worker(IOProvider io) {
        return new Worker(validationName(io), coordinates.coordinates(io), validationSalary(io),
                validationStartDate(io), validationEndDate(io), status.status(io), organization.organization(io));
    }
}
