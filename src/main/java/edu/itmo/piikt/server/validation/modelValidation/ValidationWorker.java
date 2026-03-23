package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.time.*;
import java.util.Optional;
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
 * @version 2.0
 *
 * @see Function
 * @see Builder
 * @see ValidationAddress
 * @see ValidationOrganization
 * @see ValidationCoordinates
 * @see ValidationStatus
 * @see ZonedDateTime
 * @see LocalDate
 */
public class ValidationWorker {
    private ValidationCoordinates coordinates;
    private ValidationStatus status;
    private ValidationOrganization organization;
    private final Function<String, Optional<MessageExceptionValidation>> nameValidation;
    private final Function<String, Optional<MessageExceptionValidation>> salaryValidation;
    private final Function<String, Optional<MessageExceptionValidation>> startDateValidation;
    private final Function<String, Optional<MessageExceptionValidation>> endDateValidation;

    public ValidationWorker() {
        this.coordinates = new ValidationCoordinates();
        this.status = new ValidationStatus();
        this.organization = new ValidationOrganization();

        this.startDateValidation = new Builder<String>("start date").add(RulesValidation.blank())
                .add(RulesValidation.localDate()).build();

        this.endDateValidation = new Builder<String>("end date").add(RulesValidation.validationDate()).build();

        this.nameValidation = new Builder<String>("name").add(RulesValidation.blank()).build();

        this.salaryValidation = new Builder<String>("salary").add(RulesValidation.validationSalary()).build();
    }

    public Optional<MessageExceptionValidation> validationName(String name) {
        return nameValidation.apply(name);
    }

    public Optional<MessageExceptionValidation> validationSalary(String salary) {
        return salaryValidation.apply(salary);
    }

    public Optional<MessageExceptionValidation> validationStartDate(String startDate) {
        return startDateValidation.apply(startDate);
    }

    public Optional<MessageExceptionValidation> validationEndDate(String endDate) {
        return endDateValidation.apply(endDate);
    }

    /**
     * The method creates an employee considering all validations.
     *
     * @return Worker
     */
    public Worker worker(WorkerData data) {
        return new Worker();
    }
}
