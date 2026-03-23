package edu.itmo.piikt.client.data;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.server_client.ServerResponse;

public class OrganizationServer {
    private IOProvider io;
    public OrganizationData build(ServerResponse serverResponse) {
        OrganizationData organizationData = (OrganizationData) serverResponse.getDataString();
        if (organizationData != null) {
            if (organizationData.getAnnualTurnover() == null) {
                io.println("Введите доход компании");
                String annualTurnover = io.readLine();
                organizationData.setAnnualTurnover(annualTurnover);
            }
            if (organizationData.getType().getId() == null) {
                io.println("Введите номер статуса повторно*");
                String type = io.readLine();
                organizationData.getType().setId(type);
            }
            if (organizationData.getOfficialAddress().getStreet() == null) {
                io.println("Введите адрес повторно*");
                String address = io.readLine();
                organizationData.getOfficialAddress().setStreet(address);
            }
        }
        return organizationData;
    }
}
