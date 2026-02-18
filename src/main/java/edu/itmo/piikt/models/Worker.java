package edu.itmo.piikt.models;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Comparator;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

import edu.itmo.piikt.validationModels.GeneratorId;
import edu.itmo.piikt.validationModels.ValidationWorker;

public class Worker implements Comparable<Worker> {
    @CsvBindByPosition(position = 0)
    private int id;
    @CsvBindByPosition(position = 1)//Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    @CsvRecurse
    private Coordinates coordinates; //Поле не может быть null
    @CsvBindByPosition(position = 4)
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @CsvBindByPosition(position = 5)
    private Float salary; //Поле может быть null, Значение поля должно быть больше 0
    @CsvBindByPosition(position = 6)
    private java.util.Date startDate; //Поле не может быть null
    @CsvBindByPosition(position = 7)
    private java.time.ZonedDateTime endDate;
    @CsvBindByPosition(position = 8)
    private Status status; //Поле не может быть null
    @CsvRecurse
    private Organization organization;

    public Worker(String name, Coordinates coordinates, Float salary,
                  Date startDate, ZonedDateTime endDate, Status status,
                  Organization organization){
        this.id = GeneratorId.getInstance().getId(); //генерация в отдельном классе
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "id: " + id +", name: " + name + ", координаты: " + coordinates.toString() +
                ", дата идентификации: " + creationDate + ", заработная плата: " + salary +
                ", дата начала работы: " + startDate + ", дата увольнения: " + endDate +
                ", статус: " + status.toString() + ", организация: " + organization.toString() + "\n";
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

    public Date getStartDate() {
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Worker other) {
        if (this.endDate != null && other.endDate != null) {
            return this.endDate.compareTo(other.endDate);
        } else if (other.endDate == null) {
            return 1;
        } else if (this.endDate == null) {
            return -1;
        } else
            return Integer.compare(this.id, other.id);
    }
}