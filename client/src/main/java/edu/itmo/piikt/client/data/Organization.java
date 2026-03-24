package edu.itmo.piikt.client.data;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.data.AddressData;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.data.OrganizationType.TypeOrganizationDate;

public class Organization {
    public OrganizationData build(IOProvider io) {
        io.println("Введите годовой доход компании");
        String annualTurnover = io.readLine();
        io.println("Выбирите один из типов компании и введите его номер");
        String type = io.readLine();
        io.println("Введите адрес компании");
        String address = io.readLine();
        AddressData addressData = new AddressData(address);
        TypeOrganizationDate typeData = new TypeOrganizationDate(type);
        OrganizationData organizationData = new OrganizationData(annualTurnover, typeData, addressData);
        return organizationData;
    }
}
