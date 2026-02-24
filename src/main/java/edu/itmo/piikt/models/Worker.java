package edu.itmo.piikt.models;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Comparator;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;
import com.opencsv.bean.CsvDate;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.validationModels.GeneratorId;
import edu.itmo.piikt.validationModels.ValidationWorker;

/**
 * The class of the Worker type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class Worker implements Comparable<Worker> {
    private IOProvider io;
    @CsvBindByPosition(position = 0)
    private int id;
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

    public Worker(String name, Coordinates coordinates, Float salary,
                  LocalDate startDate, ZonedDateTime endDate, Status status,
                  Organization organization){
        this.id = GeneratorId.getInstance(io).getId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.organization = organization;
    }

    public Worker() {
    }

    /**
     * Returns a brief description of this Worker. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "id: id, name: name, coordinates: coordinates, creationDate: creationDate, salary: salary,
     * startDate: startDate, endDate: endDate, status: status, organization: organization"
     */

    @Override
    public String toString() {
        return "id: " + id +", name: " + name + ", coordinates: " + coordinates.toString() +
                ", creationDate: " + creationDate + ", salary: " + salary +
                ", \nstartDate: " + startDate + ", endDate: " + endDate +
                ", status: " + status.toString() + ", organization: " + organization.toString() + "\n";
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Float getSalary() {
        return salary;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Worker other) {
        return Integer.compare(this.id, other.id);
    }
}