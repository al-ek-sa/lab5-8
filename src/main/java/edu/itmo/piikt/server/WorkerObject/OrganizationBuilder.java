package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.common.models.OrganizationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrganizationBuilder {
    public Organization organizationBuilder(OrganizationData organizationData) {
        Integer annualTurnover = Integer.parseInt(organizationData.getAnnualTurnover());
        OrganizationType organizationType = OrganizationType
                .values()[Integer.parseInt(organizationData.getType().getId()) - 1];
        Address address = new Address(organizationData.getOfficialAddress().getStreet());
        return new Organization(annualTurnover, organizationType, address);
    }
}
