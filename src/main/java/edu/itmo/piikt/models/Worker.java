package edu.itmo.piikt.models;

import java.time.ZonedDateTime;
import java.util.Date;

import edu.itmo.piikt.validationModels.GeneratorId;

public class Worker {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float salary; //Поле может быть null, Значение поля должно быть больше 0
    private java.util.Date startDate; //Поле не может быть null
    private java.time.ZonedDateTime endDate;
    private Status status; //Поле не может быть null
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
                ", статус: " + status.toString() + ", организация: " + organization.toString();
    }
}