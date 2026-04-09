package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Status;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum Status.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see Function
 * @see Builder
 * @see Status
 */
public class ValidationStatus {
	private static final AppLogger logger = new AppLogger(ValidationStatus.class);
	private final Function<String, Optional<MessageExceptionValidation>> statusValidation;

	public ValidationStatus() {
		this.statusValidation = new Builder<String>("status").add(RulesValidation.validationStatus()).build();
		logger.debug("ValidationStatus initialized");
	}

	/**
	 * Validates the status field
	 *
	 * @param status
	 *            status value to validate
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationStatus(String status) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating status: {}", status);
			return statusValidation.apply(status);
		} catch (Exception e) {
			logger.error("Error validating status: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("status", "Validation error: " + e.getMessage()));
		}
	}
}
