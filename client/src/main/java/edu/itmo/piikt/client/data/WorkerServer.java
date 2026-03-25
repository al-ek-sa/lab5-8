package edu.itmo.piikt.client.data;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.data.Status.StatusData;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerServer {
    private IOProvider io;
    // todo ошибка при приведении типов, привидение типов, есть поля которые могут
    // быть null по умолчанию
    public WorkerData build(ServerResponse serverResponse) {
        WorkerData workerData = (WorkerData) serverResponse.getDataString();
        List<MessageExceptionValidation> list = serverResponse.getErrors();
        if (workerData.getName() == null) {
            io.println(WorkerPrint.NAME.getMessageError());
            String name = io.readLine();
            workerData.setName(name);
        }
        if (workerData.getCoordinates().getX() == null) {
            io.println(WorkerPrint.X.getMessageError());
            String x = io.readLine();
            workerData.getCoordinates().setX(x);
        }
        if (workerData.getCoordinates().getY() == null) {
            io.println(WorkerPrint.Y.getMessageError());
            String y = io.readLine();
            workerData.getCoordinates().setY(y);
        }
        if (workerData.getSalary() == null) {
            io.println(WorkerPrint.SALARY.getMessageError());
            String salary = io.readLine();
            workerData.setSalary(salary);
        }
        if (workerData.getStartDate() == null) {
            io.println(WorkerPrint.START_DATE.getMessageError());
            String startDate = io.readLine();
            workerData.setStartDate(startDate);
        }
        if (workerData.getEndDate() == null) {
            io.println(WorkerPrint.END_DATE.getMessageError());
            String endDate = io.readLine();
            workerData.setEndDate(endDate);
        }
        if (workerData.getStatus().getId() == null) {
            io.println(WorkerPrint.STATUS.getMessageError());
            Arrays.stream(StatusData.values()).forEach(statusData -> io.println(statusData.getId() + ": " + statusData.name()));
            String status = io.readLine();
            workerData.getStatus().setId(status);
        }
        if (workerData.getOrganization() != null) {
            if (workerData.getOrganization().getAnnualTurnover() == null) {
                io.println(WorkerPrint.ANNUAL_TURNOVER.getMessageError());
                String annualTurnover = io.readLine();
                workerData.getOrganization().setAnnualTurnover(annualTurnover);
            }
            if (workerData.getOrganization().getType().getId() == null) {
                io.println(WorkerPrint.TYPE.getMessageError());
                Arrays.stream(OrganizationTypeData.values()).forEach(type -> io.println(type.getId() + ": " + type.name()));
                String type = io.readLine();
                workerData.getOrganization().getType().setId(type);
            }
            if (workerData.getOrganization().getOfficialAddress().getStreet() == null) {
                io.println(WorkerPrint.STREET.getMessageError());
                String address = io.readLine();
                workerData.getOrganization().getOfficialAddress().setStreet(address);
            }
        }
        return workerData;
    }
}
