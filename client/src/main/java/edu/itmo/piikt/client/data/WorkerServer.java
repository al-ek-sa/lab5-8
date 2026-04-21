package edu.itmo.piikt.client.data;

import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.data.Status.StatusData;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ServerResponse;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Builder for correcting Worker data when validation fails
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerServer {
	private static final AppLogger logger = new AppLogger(WorkerServer.class);
	private IOProvider io;

	/**
	 * Builds corrected WorkerData based on validation errors
	 *
	 * @param serverResponse
	 *            server response containing partial WorkerData
	 * @return corrected WorkerData
	 */
	public WorkerData build(ServerResponse serverResponse) {
		try (Context ignored = Context.newId()) {
			logger.debug("Building worker data from server response");
			Object dataObj = serverResponse.dataString();
			if (dataObj == null) {
				logger.error("Server response contains no data");
				throw new RuntimeException("No data from server for correction");
			}
			WorkerData workerData = (WorkerData) serverResponse.dataString();
			if (workerData.getName() == null) {
				logger.debug("Name is null, requesting input");
				io.println(WorkerPrint.NAME.getMessageError());
				String name = io.readLine();
				while (name.length() > 75) {
					io.println("The string you entered is too long, please try entering it again.");
					name = io.readLine();
				}
				workerData.setName(name);
			}
			if (workerData.getCoordinates().getX() == null) {
				logger.debug("Coordinate X is null, requesting input");
				io.println(WorkerPrint.X.getMessageError());
				String x = io.readLine();
				while (x.length() > 20) {
					io.println("The string you entered is too long, please try entering it again.");
					x = io.readLine();
				}
				workerData.getCoordinates().setX(x);
			}
			if (workerData.getCoordinates().getY() == null) {
				logger.debug("Coordinate Y is null, requesting input");
				io.println(WorkerPrint.Y.getMessageError());
				String y = io.readLine();
				while (y.length() > 20) {
					io.println("The string you entered is too long, please try entering it again.");
					y = io.readLine();
				}
				workerData.getCoordinates().setY(y);
			}
			if (workerData.getSalary() == null) {
				logger.debug("Salary is null, requesting input");
				io.println(WorkerPrint.SALARY.getMessageError());
				String salary = io.readLine();
				while (salary.length() > 10) {
					io.println("The string you entered is too long, please try entering it again.");
					salary = io.readLine();
				}
				workerData.setSalary(salary);
			}
			if (workerData.getStartDate() == null) {
				logger.debug("Start date is null, requesting input");
				io.println(WorkerPrint.START_DATE.getMessageError());
				String startDate = io.readLine();
				while (startDate.length() > 10) {
					io.println("The string you entered is too long, please try entering it again.");
					startDate = io.readLine();
				}
				workerData.setStartDate(startDate);
			}
			if (workerData.getEndDate() == null) {
				logger.debug("End date is null, requesting input");
				io.println(WorkerPrint.END_DATE.getMessageError());
				String endDate = io.readLine();
				while (endDate.length() < 10) {
					io.println("The string you entered is too long, please try entering it again.");
					endDate = io.readLine();
				}
				workerData.setEndDate(endDate);
			}
			if (workerData.getStatus().getId() == null) {
				logger.debug("Status is null, requesting input");
				io.println(WorkerPrint.STATUS.getMessageError());
				Arrays.stream(StatusData.values())
						.forEach(statusData -> io.println(statusData.getId() + ": " + statusData.name()));
				String status = io.readLine();
				while (status.length() > 5) {
					io.println("The string you entered is too long, please try entering it again.");
					status = io.readLine();
				}
				workerData.getStatus().setId(status);
			}
			if (workerData.getOrganization() != null) {
				if (workerData.getOrganization().getAnnualTurnover() == null) {
					logger.debug("Organization annual turnover is null, requesting input");
					io.println(WorkerPrint.ANNUAL_TURNOVER.getMessageError());
					String annualTurnover = io.readLine();
					while (annualTurnover.length() > 10) {
						io.println("The string you entered is too long, please try entering it again.");
						annualTurnover = io.readLine();
					}
					workerData.getOrganization().setAnnualTurnover(annualTurnover);
				}
				if (workerData.getOrganization().getType().getId() == null) {
					logger.debug("Organization type is null, requesting input");
					io.println(WorkerPrint.TYPE.getMessageError());
					Arrays.stream(OrganizationTypeData.values())
							.forEach(type -> io.println(type.getId() + ": " + type.name()));
					String type = io.readLine();
					while (type.length() > 5) {
						io.println("The string you entered is too long, please try entering it again.");
						type = io.readLine();
					}
					workerData.getOrganization().getType().setId(type);
				}
				if (workerData.getOrganization().getOfficialAddress().getStreet() == null) {
					logger.debug("Organization street is null, requesting input");
					io.println(WorkerPrint.STREET.getMessageError());
					String address = io.readLine();
					while (address.length() > 100) {
						io.println("The string you entered is too long, please try entering it again.");
						address = io.readLine();
					}
					workerData.getOrganization().getOfficialAddress().setStreet(address);
				}
			}
			logger.debug("Worker data corrected");
			return workerData;
		} catch (Exception e) {
			logger.error("Error building worker data: {}", e);
			throw new RuntimeException(e);
		}
	}
}
