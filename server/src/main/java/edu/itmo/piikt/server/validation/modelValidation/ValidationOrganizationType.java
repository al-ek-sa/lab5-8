package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum OrganizationType.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see Function
 * @see Builder
 * @see OrganizationType
 */
public class ValidationOrganizationType {
	private static final AppLogger logger = new AppLogger(ValidationOrganizationType.class);
	/** Validation function for organization type field */
	private final Function<String, Optional<MessageExceptionValidation>> organizationValidation;

	public ValidationOrganizationType() {
		this.organizationValidation = new Builder<String>("organization type").add(RulesValidation.validationType())
				.build();
		logger.debug("ValidationOrganizationType initialized");
	}

	/**
	 * Validates the organization type field
	 *
	 * @param type
	 *            organization type value to validate
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationOrganizationType(String type) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating organization type: {}", type);
			return organizationValidation.apply(type);
		} catch (Exception e) {
			logger.error("Error validating organization type: {}", e.getMessage());
			return Optional
					.of(new MessageExceptionValidation("organization type", "Validation error: " + e.getMessage()));
		}
	}
}
