package edu.itmo.piikt.client.data;

import edu.itmo.piikt.common.data.*;
import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.data.OrganizationType.TypeOrganizationDate;
import edu.itmo.piikt.common.data.Status.DataStatus;
import edu.itmo.piikt.common.data.Status.StatusData;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import java.util.Arrays;
import lombok.NoArgsConstructor;

/**
 * Builder for Worker data
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@NoArgsConstructor
public class Worker {
	private static final AppLogger logger = new AppLogger(Worker.class);

	/**
	 * Builds WorkerData from user input
	 *
	 * @param io
	 *            input/output provider for user interaction
	 * @return built WorkerData
	 */
	public WorkerData build(IOProvider io) {
		try (Context ignored = Context.newId()) {
			logger.debug("Building worker data");
			io.println(WorkerPrint.NAME.getMessage());
			String name = io.readLine();
			while (name.length() > 75) {
				io.println("The string you entered is too long, please try entering it again.");
				name = io.readLine();
			}
			io.println(WorkerPrint.X.getMessage());
			String x = io.readLine();
			while (x.length() > 20) {
				io.println("The string you entered is too long, please try entering it again.");
				x = io.readLine();
			}
			io.println(WorkerPrint.Y.getMessage());
			String y = io.readLine();
			while (y.length() > 20) {
				io.println("The string you entered is too long, please try entering it again.");
				y = io.readLine();
			}
			io.println(WorkerPrint.SALARY.getMessage());
			String salary = io.readLine();
			while (salary.length() > 10) {
				io.println("The string you entered is too long, please try entering it again.");
				salary = io.readLine();
			}
			io.println(WorkerPrint.START_DATE.getMessage());
			String startDate = io.readLine();
			while (startDate.length() > 11) {
				io.println("The string you entered is too long, please try entering it again.");
				startDate = io.readLine();
			}
			io.println(WorkerPrint.END_DATE.getMessage());
			String endDate = io.readLine();
			while (endDate.length() > 11) {
				io.println("The string you entered is too long, please try entering it again.");
				endDate = io.readLine();
			}
			io.println(WorkerPrint.STATUS.getMessage());
			Arrays.stream(StatusData.values())
					.forEach(statusData -> io.println(statusData.getId() + ": " + statusData.name()));
			String status = io.readLine();
			while (status.length() > 5) {
				io.println("The string you entered is too long, please try entering it again.");
				status = io.readLine();
			}
			io.println(WorkerPrint.ANNUAL_TURNOVER.getMessage());
			String annualTurnover = io.readLine();
			while (annualTurnover.length() > 10) {
				io.println("The string you entered is too long, please try entering it again.");
				annualTurnover = io.readLine();
			}
			io.println(WorkerPrint.TYPE.getMessage());
			Arrays.stream(OrganizationTypeData.values()).forEach(type -> io.println(type.getId() + ": " + type.name()));
			String type = io.readLine();
			while (type.length() > 5) {
				io.println("The string you entered is too long, please try entering it again.");
				type = io.readLine();
			}
			io.println(WorkerPrint.STREET.getMessage());
			String address = io.readLine();
			while (address.length() > 100) {
				io.println("The string you entered is too long, please try entering it again.");
				address = io.readLine();
			}
			CoordinatesData coordinatesData = new CoordinatesData(x, y);
			AddressData addressData = new AddressData(address);
			TypeOrganizationDate typeOrganizationDate = new TypeOrganizationDate(type);
			OrganizationData organizationData = new OrganizationData(annualTurnover, typeOrganizationDate, addressData);
			DataStatus dataStatus = new DataStatus(status);
			WorkerData workerData = new WorkerData(name, coordinatesData, salary, startDate, endDate, dataStatus,
					organizationData);
			logger.debug("Worker data built: name={}, salary={}, status={}", name, salary, status);
			return workerData;
		} catch (Exception e) {
			logger.error("Error building worker data: {}", e);
			throw new RuntimeException(e);
		}
	}
}
