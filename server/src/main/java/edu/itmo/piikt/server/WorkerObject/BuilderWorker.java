package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.validation.modelValidation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Builder for validating Worker data
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Data
public class BuilderWorker implements Serializable {
	private static final AppLogger logger = new AppLogger(BuilderWorker.class);
	private ValidationWorker validationWorker;
	private ValidationStatus validationStatus;
	private ValidationOrganizationType validationOrganizationType;
	private ValidationOrganization validationOrganization;
	private ValidationAddress validationAddress;
	private ValidationCoordinates validationCoordinates;

	public BuilderWorker() {
		this.validationAddress = new ValidationAddress();
		this.validationWorker = new ValidationWorker();
		this.validationStatus = new ValidationStatus();
		this.validationOrganizationType = new ValidationOrganizationType();
		this.validationOrganization = new ValidationOrganization();
		this.validationAddress = new ValidationAddress();
		this.validationCoordinates = new ValidationCoordinates();
		logger.debug("BuilderWorker initialized");
	}

	/**
	 * Validates WorkerData and returns either the validated data or validation errors
	 * @param workerData worker data to validate
	 * @return WorkerData if valid, ValidationError with errors otherwise
	 */
	public Object data(WorkerData workerData) {
		if (workerData == null) {
			logger.error("WorkerData is null");
			return new ValidationError(List.of(new MessageExceptionValidation("worker", "Worker data is null")), null);
		}

		try (Context ignored = Context.newId()) {
			AtomicReference<WorkerData> data = new AtomicReference<>(workerData);
			List<MessageExceptionValidation> errors = new ArrayList<>();

			if (workerData.getName() != null) {
				validationWorker.validationName(workerData.getName()).ifPresent(error -> {
					errors.add(error);
					data.get().setName(null);
				});
			}
			if (workerData.getSalary() != null) {
				validationWorker.validationSalary(workerData.getSalary()).ifPresent(error -> {
					errors.add(error);
					data.get().setSalary(null);
				});
			}
			if (workerData.getEndDate() != null) {
				validationWorker.validationEndDate(workerData.getEndDate()).ifPresent(error -> {
					errors.add(error);
					data.get().setEndDate(null);
				});
			}
			if (workerData.getStartDate() != null) {
				validationWorker.validationStartDate(workerData.getStartDate()).ifPresent(error -> {
					errors.add(error);
					data.get().setStartDate(null);
				});
			}
			if (workerData.getCoordinates() != null) {
				if (workerData.getCoordinates().getX() != null) {
					validationCoordinates.validationX(workerData.getCoordinates().getX()).ifPresent(error -> {
						errors.add(error);
						data.get().getCoordinates().setX(null);
					});
				}
				if (workerData.getCoordinates().getY() != null) {
					validationCoordinates.validationY(workerData.getCoordinates().getY()).ifPresent(error -> {
						errors.add(error);
						data.get().getCoordinates().setY(null);
					});
				}
			}
			if (workerData.getStatus() != null && workerData.getStatus().getId() != null) {
				validationStatus.validationStatus(workerData.getStatus().getId()).ifPresent(error -> {
					errors.add(error);
					data.get().getStatus().setId(null);
				});
			}
			if (workerData.getOrganization() != null) {
				if (workerData.getOrganization().getAnnualTurnover() != null) {
					validationOrganization.validationAnnualTurnover(workerData.getOrganization().getAnnualTurnover())
							.ifPresent(error -> {
								errors.add(error);
								data.get().getOrganization().setAnnualTurnover(null);
							});
				}
				if (workerData.getOrganization().getType() != null
						&& workerData.getOrganization().getType().getId() != null) {
					validationOrganizationType
							.validationOrganizationType(workerData.getOrganization().getType().getId())
							.ifPresent(error -> {
								errors.add(error);
								data.get().getOrganization().getType().setId(null);
							});
				}
				if (workerData.getOrganization().getOfficialAddress() != null
						&& workerData.getOrganization().getOfficialAddress().getStreet() != null) {
					validationAddress.validation(workerData.getOrganization().getOfficialAddress().getStreet())
							.ifPresent(error -> {
								errors.add(error);
								data.get().getOrganization().getOfficialAddress().setStreet(null);
							});
				}
			}
			if (errors.isEmpty()) {
				logger.debug("Worker validation passed");
				return workerData;
			} else {
				logger.warn("Worker validation failed: {} errors", errors.size());
				return new ValidationError(errors, data);
			}
		} catch (Exception e) {
			logger.error("Error validating worker: {}", e.getMessage());
			List<MessageExceptionValidation> error = List
					.of(new MessageExceptionValidation("worker", "Validation error: " + e.getMessage()));
			return new ValidationError(error, workerData);
		}
	}
}
