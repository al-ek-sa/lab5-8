package edu.itmo.piikt.common.data;

import edu.itmo.piikt.common.data.OrganizationType.TypeOrganizationDate;
import java.io.Serial;
import java.io.Serializable;
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
public class OrganizationData implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String annualTurnover;
	private TypeOrganizationDate type;
	private AddressData officialAddress;
}
