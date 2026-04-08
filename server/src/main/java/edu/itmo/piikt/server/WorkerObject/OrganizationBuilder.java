package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.common.models.OrganizationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrganizationBuilder {
	private static final AppLogger logger = new AppLogger(OrganizationBuilder.class);

	public Organization organizationBuilder(OrganizationData organizationData) {
		try (Context ignored = Context.newId()) {
			logger.debug("Building organization from data");
			int annualTurnover = Integer.parseInt(organizationData.getAnnualTurnover());
			OrganizationType organizationType = OrganizationType
					.values()[Integer.parseInt(organizationData.getType().getId()) - 1];
			Address address = new Address(organizationData.getOfficialAddress().getStreet());
			logger.debug("Organization built: turnover={}, type={}, street={}", annualTurnover, organizationType,
					address.getStreet());
			return new Organization(annualTurnover, organizationType, address);
		} catch (Exception e) {
			logger.error("Error building organization: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
