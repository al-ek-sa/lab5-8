package edu.itmo.piikt.client.data;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.data.*;
import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.data.OrganizationType.TypeOrganizationDate;
import edu.itmo.piikt.common.data.Status.DataStatus;
import edu.itmo.piikt.common.data.Status.StatusData;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
public class Worker {
    private static final AppLogger logger = new AppLogger(Worker.class);

    public WorkerData build(IOProvider io) {
        try (Context ignored = Context.newId()) {
            logger.debug("Building worker data");
            io.println(WorkerPrint.NAME.getMessage());
            String name = io.readLine();
            io.println(WorkerPrint.X.getMessage());
            String x = io.readLine();
            io.println(WorkerPrint.Y.getMessage());
            String y = io.readLine();
            io.println(WorkerPrint.SALARY.getMessage());
            String salary = io.readLine();
            io.println(WorkerPrint.START_DATE.getMessage());
            String startDate = io.readLine();
            io.println(WorkerPrint.END_DATE.getMessage());
            String endDate = io.readLine();
            io.println(WorkerPrint.STATUS.getMessage());
            Arrays.stream(StatusData.values()).forEach(statusData -> io.println(statusData.getId() + ": " + statusData.name()));
            String status = io.readLine();
            io.println(WorkerPrint.ANNUAL_TURNOVER.getMessage());
            String annualTurnover = io.readLine();
            io.println(WorkerPrint.TYPE.getMessage());
            Arrays.stream(OrganizationTypeData.values()).forEach(type -> io.println(type.getId() + ": " + type.name()));
            String type = io.readLine();
            io.println(WorkerPrint.STREET.getMessage());
            String address = io.readLine();
            CoordinatesData coordinatesData = new CoordinatesData(x, y);
            AddressData addressData = new AddressData(address);
            TypeOrganizationDate typeOrganizationDate = new TypeOrganizationDate(type);
            OrganizationData organizationData = new OrganizationData(annualTurnover, typeOrganizationDate, addressData);
            DataStatus dataStatus = new DataStatus(status);
            WorkerData workerData = new WorkerData(name, coordinatesData, salary, startDate, endDate, dataStatus,
                    organizationData);
            logger.debug("Worker data built: name={}, salary={}, status={}", name, salary, status);
            return workerData;
        } catch (Exception e) {
            logger.error("Error building worker data: {}", e);
            throw new RuntimeException(e);
        }
    }
}