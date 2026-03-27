package edu.itmo.piikt.client.data;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationServer {
    private IOProvider io;
    public OrganizationData build(ServerResponse serverResponse) {
        OrganizationData organizationData = (OrganizationData) serverResponse.getDataString();

            if (organizationData.getAnnualTurnover() == null) {
                io.println(WorkerPrint.ANNUAL_TURNOVER.getMessageError());
                String annualTurnover = io.readLine();
                organizationData.setAnnualTurnover(annualTurnover);
            }
            if (organizationData.getType().getId() == null) {
                io.println(WorkerPrint.TYPE.getMessageError());
                Arrays.stream(OrganizationTypeData.values()).forEach(type -> io.println(type.getId() + ": " + type.name()));
                String type = io.readLine();
                organizationData.getType().setId(type);
            }
            if (organizationData.getOfficialAddress().getStreet() == null) {
                io.println(WorkerPrint.STREET.getMessageError());
                String address = io.readLine();
                organizationData.getOfficialAddress().setStreet(address);

        }
        return organizationData;
    }
}
