package edu.itmo.piikt.client.data;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.data.*;
import edu.itmo.piikt.common.data.OrganizationType.OrganizationTypeData;
import edu.itmo.piikt.common.data.OrganizationType.TypeOrganizationDate;
import edu.itmo.piikt.common.data.Status.DataStatus;
import edu.itmo.piikt.common.data.Status.StatusData;
import edu.itmo.piikt.common.models.Status;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
public class Worker {
    public WorkerData build(IOProvider io) {
        io.println("Введите имя*");
        String name = io.readLine();
        io.println("Введите координату х* (число должно быть меньше 10)");
        String x = io.readLine();
        io.println("Введите у* (число должно быть больше -644)");
        String y = io.readLine();
        io.println("Введите зп*");
        String salary = io.readLine();
        io.println("Введите дату начала работы* (Пример ввода: 1111-11-11)");
        String startDate = io.readLine();
        io.println("Выбирите дату окончания работы (Пример ввода: 1111-11-11");
        String endDate = io.readLine();
        io.println("Выбирите статус и ввдите его номер*");
        Arrays.stream(StatusData.values()).forEach(statusData -> io.println(statusData.getId() + ": " + statusData.name()));
        String status = io.readLine();
        io.println("Введите годовой доход компании");
        String annualTurnover = io.readLine();
        io.println("Выбирите один из типов компании и введите его номер");
        Arrays.stream(OrganizationTypeData.values()).forEach(type -> io.println(type.getId() + ": " + type.name()));
        String type = io.readLine();
        io.println("Введите адрес компании");
        String address = io.readLine();
        CoordinatesData coordinatesData = new CoordinatesData(x, y);
        AddressData addressData = new AddressData(address);
        TypeOrganizationDate typeOrganizationDate = new TypeOrganizationDate(type);
        OrganizationData organizationData = new OrganizationData(annualTurnover, typeOrganizationDate, addressData);
        DataStatus dataStatus = new DataStatus(status);
        WorkerData workerData = new WorkerData(name, coordinatesData, salary, startDate, endDate, dataStatus,
                organizationData);
        return workerData;
    }
}
