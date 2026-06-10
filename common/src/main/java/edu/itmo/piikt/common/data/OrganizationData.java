package edu.itmo.piikt.common.data;

import edu.itmo.piikt.common.data.organization.type.TypeOrganizationDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for organization information
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationData {
	private String annualTurnover;
	private TypeOrganizationDate type;
	private AddressData officialAddress;
}
