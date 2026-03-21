package edu.itmo.piikt.common.models;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvRecurse;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.util.GeneratorId;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * The class of the Worker type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Worker implements Comparable<Worker>, Serializable {
    private static final long serialVersionUID = 1L;
    @CsvBindByPosition(position = 0)
    private String uuid;

    @CsvBindByPosition(position = 1)
    private String name;

    @CsvRecurse
    private Coordinates coordinates;

    @CsvBindByPosition(position = 4)
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private java.util.Date creationDate;

    @CsvBindByPosition(position = 5)
    private Float salary;

    @CsvBindByPosition(position = 6)
    @CsvDate("yyyy-MM-dd")
    private java.time.LocalDate startDate;

    @CsvBindByPosition(position = 7)
    @CsvDate("yyyy-MM-dd'T'HH:mm:ssXXX")
    private java.time.ZonedDateTime endDate;

    @CsvBindByPosition(position = 8)
    private Status status;

    @CsvRecurse
    private Organization organization;

    public Worker(WorkerData data) {
        this.uuid = GeneratorId.getId();
        this.name = data.getName();
        this.coordinates = data.getCoordinates();
        this.creationDate = new Date();
        this.salary = data.getSalary();
        this.startDate = data.getStartDate();
        this.endDate = data.getEndDate();
        this.status = data.getStatus();
        this.organization = data.getOrganization();
    }

    /**
     * Returns a brief description of this Worker. The exact details of the
     * representation are unspecified and subject to change, but the following may
     * be regarded as typical:
     *
     * <p>
     * "id: id, name: name, coordinates: coordinates, creationDate: creationDate,
     * salary: salary, startDate: startDate, endDate: endDate, status: status,
     * organization: organization"
     */
    @Override
    public String toString() {
        return "id: " + uuid + ", name: " + name + ", coordinates: "
                + (coordinates == null ? "null" : coordinates.toString()) + ", creationDate: " + creationDate
                + ", salary: " + salary + ", \nstartDate: " + startDate + ", endDate: " + endDate + ", status: "
                + (status == null ? "null" : status.toString()) + ", organization: "
                + (organization == null ? "null" : organization.toString()) + "\n";
    }
    @Override
    public int compareTo(Worker other) {
        return this.uuid.compareTo(other.uuid);
    }
}
