package edu.itmo.piikt.client.data;

import edu.itmo.piikt.common.data.AddressData;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.data.organization.type.OrganizationTypeData;
import edu.itmo.piikt.common.data.organization.type.TypeOrganizationDate;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import java.util.Arrays;

/**
 * Collects Organization data from user input through IOProvider Collects
 * Organization data from user input through IOProvider
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class Organization {
	private static final AppLogger logger = new AppLogger(Organization.class);

	/**
	 * Builds OrganizationData from user input
	 *
	 * @param io
	 *            input/output provider for user interaction
	 * @return built OrganizationData
	 */
	public OrganizationData build(IOProvider io) {
		try (Context ignored = Context.newId()) {
			logger.debug("Building organization data");
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
			AddressData addressData = new AddressData(address);
			TypeOrganizationDate typeData = new TypeOrganizationDate(type);
			OrganizationData organizationData = new OrganizationData(annualTurnover, typeData, addressData);
			logger.debug("Organization data built: turnover={}, type={}, street={}", annualTurnover, type, address);
			return organizationData;
		} catch (Exception e) {
			logger.error("Error building organization data: {}", e);
			throw new RuntimeException(e);
		}
	}
}
