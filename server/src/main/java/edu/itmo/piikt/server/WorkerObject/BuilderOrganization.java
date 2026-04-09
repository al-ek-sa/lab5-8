package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.validation.modelValidation.ValidationAddress;
import edu.itmo.piikt.server.validation.modelValidation.ValidationOrganization;
import edu.itmo.piikt.server.validation.modelValidation.ValidationOrganizationType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Builder for validating Organization data
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class BuilderOrganization implements Serializable {
	private static final AppLogger logger = new AppLogger(BuilderOrganization.class);
	private ValidationOrganizationType validationOrganizationType;
	private ValidationOrganization validationOrganization;
	private ValidationAddress validationAddress;

	public BuilderOrganization() {
		this.validationAddress = new ValidationAddress();
		this.validationOrganization = new ValidationOrganization();
		this.validationOrganizationType = new ValidationOrganizationType();
		logger.debug("BuilderOrganization initialized");
	}

	/**
	 * Validates OrganizationData and returns either the validated data or
	 * validation errors
	 *
	 * @param organizationData
	 *            organization data to validate
	 * @return OrganizationData if valid, ValidationError with errors otherwise
	 */
	public Object data(OrganizationData organizationData) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating organization data");
			List<MessageExceptionValidation> errors = new ArrayList<>();

			validationOrganizationType.validationOrganizationType(organizationData.getType().getId())
					.ifPresent(errors::add);
			validationAddress.validation(organizationData.getOfficialAddress().getStreet()).ifPresent(errors::add);
			validationOrganization.validationAnnualTurnover(organizationData.getAnnualTurnover())
					.ifPresent(errors::add);

			if (errors.isEmpty()) {
				logger.debug("Organization validation passed");
				return organizationData;
			} else {
				logger.warn("Organization validation failed: {} errors", errors.size());
				return new ValidationError(errors, organizationData);
			}
		} catch (Exception e) {
			logger.error("Error validating organization: {}", e.getMessage());
			List<MessageExceptionValidation> error = List
					.of(new MessageExceptionValidation("organization", "Validation error: " + e.getMessage()));
			return new ValidationError(error, organizationData);
		}
	}
}
