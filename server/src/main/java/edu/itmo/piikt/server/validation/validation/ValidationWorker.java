package edu.itmo.piikt.server.validation.validation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
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
 * <li>private Coordinates; //The field cannot be null
 * <li>private java.util.Date creationDate; //The field cannot be null, The
 * value of this field must be generated automatically
 * <li>private Float salary; //The field can be null, The field value must be
 * greater than 0
 * <li>private java.time.LocalDate startDate; //The field cannot be null
 * <li>private java.time.ZonedDateTime endDate; //The field can be null
 * <li>private Status; //The field cannot be null
 * <li>private Organization; //The field can be null
 * </ul>
 *
 * <p>
 * The class provides methods that validate the field values.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
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
	private static final AppLogger logger = new AppLogger(ValidationWorker.class);
	/** Validation function for name field */
	private final Function<String, Optional<MessageExceptionValidation>> nameValidation;
	/** Validation function for salary field */
	private final Function<String, Optional<MessageExceptionValidation>> salaryValidation;
	/** Validation function for start date field */
	private final Function<String, Optional<MessageExceptionValidation>> startDateValidation;
	/** Validation function for end date field */
	private final Function<String, Optional<MessageExceptionValidation>> endDateValidation;

	public ValidationWorker() {
		this.startDateValidation = new Builder<String>("start date").add(RulesValidation.blank())
				.add(RulesValidation.localDate()).build();
		this.endDateValidation = new Builder<String>("end date").add(RulesValidation.validationDate()).build();
		this.nameValidation = new Builder<String>("name").add(RulesValidation.blank()).build();
		this.salaryValidation = new Builder<String>("salary").add(RulesValidation.validationSalary()).build();
		logger.debug("ValidationWorker initialized");
	}

	/**
	 * Validates the name field
	 *
	 * @param name
	 *            name value to validate
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationName(String name) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating name: {}", name);
			return nameValidation.apply(name);
		} catch (Exception e) {
			logger.error("Error validating name: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("name", "Validation error: " + e.getMessage()));
		}
	}

	/**
	 * Validates the salary field
	 *
	 * @param salary
	 *            salary value to validate
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationSalary(String salary) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating salary: {}", salary);
			return salaryValidation.apply(salary);
		} catch (Exception e) {
			logger.error("Error validating salary: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("salary", "Validation error: " + e.getMessage()));
		}
	}

	/**
	 * Validates the start date field
	 *
	 * @param startDate
	 *            start date value to validate
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationStartDate(String startDate) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating start date: {}", startDate);
			return startDateValidation.apply(startDate);
		} catch (Exception e) {
			logger.error("Error validating start date: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("start date", "Validation error: " + e.getMessage()));
		}
	}

	/**
	 * Validates the end date field
	 *
	 * @param endDate
	 *            end date value to validate
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationEndDate(String endDate) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating end date: {}", endDate);
			return endDateValidation.apply(endDate);
		} catch (Exception e) {
			logger.error("Error validating end date: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("end date", "Validation error: " + e.getMessage()));
		}
	}
}
