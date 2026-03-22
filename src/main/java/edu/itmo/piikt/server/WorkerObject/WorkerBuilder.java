package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.AddressData;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.models.*;
import edu.itmo.piikt.common.util.GeneratorId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@NoArgsConstructor
@Data
public class WorkerBuilder {
    //todo возращает null
    public Worker builerWorker(WorkerData workerData) {
        String uuid = GeneratorId.getId();
        String name = workerData.getName();
        Coordinates coordinates = new Coordinates(
                Long.parseLong(workerData.getCoordinates().getX()),
                Float.parseFloat(workerData.getCoordinates().getY())
        );
        Date creationDate = new Date();
        Float salary = workerData.getSalary() != null && !workerData.getSalary().trim().isEmpty()
                ? Float.parseFloat(workerData.getSalary())
                : null;
        LocalDate startDate = LocalDate.parse(workerData.getStartDate());
        ZonedDateTime endDate = workerData.getEndDate() != null && !workerData.getEndDate().trim().isEmpty()
                ? ZonedDateTime.parse(workerData.getEndDate())
                : null;
        Status status = Status.values()[Integer.parseInt(workerData.getStatus().getId()) - 1];
        Organization organization = null;
        if (workerData.getOrganization().getAnnualTurnover() != null && !workerData.getOrganization().getAnnualTurnover().trim().isEmpty()) {
            Integer annualTurnover = Integer.parseInt(workerData.getOrganization().getAnnualTurnover());
            OrganizationType type = OrganizationType.values()[Integer.parseInt(workerData.getOrganization().getType().getId()) -1];
            Address address = new Address(workerData.getOrganization().getOfficialAddress().getStreet());
            organization = new Organization(annualTurnover, type, address);
        }
        return new Worker(uuid, name, coordinates, creationDate, salary, startDate, endDate, status, organization);
    }
}
