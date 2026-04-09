package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.*;
import edu.itmo.piikt.common.util.GeneratorId;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Builder for creating Worker objects from WorkerData
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class WorkerBuilder {
	private static final AppLogger logger = new AppLogger(WorkerBuilder.class);

	/**
	 * Builds a Worker entity from WorkerData
	 *
	 * @param workerData
	 *            data transfer object containing worker information
	 * @return built Worker entity
	 * @throws RuntimeException
	 *             if parsing fails
	 */
	public Worker builerWorker(WorkerData workerData) {
		try (Context ignored = Context.newId()) {
			logger.debug("Building worker from data");
			String uuid = GeneratorId.getId();
			String name = workerData.getName();
			Coordinates coordinates = new Coordinates(Long.parseLong(workerData.getCoordinates().getX()),
					Float.parseFloat(workerData.getCoordinates().getY()));
			Date creationDate = new Date();
			Float salary = workerData.getSalary() != null && !workerData.getSalary().trim().isEmpty()
					? Float.parseFloat(workerData.getSalary())
					: null;
			LocalDate startDate = LocalDate.parse(workerData.getStartDate());
			ZonedDateTime endDate = workerData.getEndDate() != null && !workerData.getEndDate().trim().isEmpty()
					? ZonedDateTime.parse(workerData.getEndDate())
					: null;
			Status status = Status.values()[Integer.parseInt(workerData.getStatus().getId()) - 1];
			Organization organization = getOrganization(workerData);
			Worker worker = new Worker(uuid, name, coordinates, creationDate, salary, startDate, endDate, status,
					organization);
			logger.debug("Worker built successfully: id={}, name={}", uuid, name);
			return worker;
		} catch (Exception e) {
			logger.error("Error building worker: {}", e.getMessage());
			throw new RuntimeException("Failed to build worker: " + e.getMessage(), e);
		}
	}

	/**
	 * Builds an Organization entity from WorkerData (maybe null)
	 *
	 * @param workerData
	 *            data transfer object containing worker information
	 * @return built Organization entity or null if organization data is incomplete
	 */
	@Nullable
	private static Organization getOrganization(WorkerData workerData) {
		Organization organization = null;
		if (workerData.getOrganization() != null && workerData.getOrganization().getAnnualTurnover() != null
				&& !workerData.getOrganization().getAnnualTurnover().trim().isEmpty()) {
			int annualTurnover = Integer.parseInt(workerData.getOrganization().getAnnualTurnover());
			OrganizationType type = OrganizationType
					.values()[Integer.parseInt(workerData.getOrganization().getType().getId()) - 1];
			Address address = new Address(workerData.getOrganization().getOfficialAddress().getStreet());
			organization = new Organization(annualTurnover, type, address);
		}
		return organization;
	}
}
