package edu.itmo.piikt.client.data;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.data.AddressData;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.data.OrganizationType.TypeOrganizationDate;
import java.util.Arrays;

public class Organization {
    public OrganizationData build(IOProvider io) {
        io.println(WorkerPrint.ANNUAL_TURNOVER.getMessage());
        String annualTurnover = io.readLine();
        io.println(WorkerPrint.TYPE.getMessage());
        Arrays.stream(OrganizationTypeData.values()).forEach(type -> io.println(type.getId() + ": " + type.name()));
        String type = io.readLine();
        io.println(WorkerPrint.STREET.getMessage());
        String address = io.readLine();
        AddressData addressData = new AddressData(address);
        TypeOrganizationDate typeData = new TypeOrganizationDate(type);
        OrganizationData organizationData = new OrganizationData(annualTurnover, typeData, addressData);
        return organizationData;
    }
}
