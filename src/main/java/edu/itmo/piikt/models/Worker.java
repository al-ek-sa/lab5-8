package edu.itmo.piikt.models;

public class Worker {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float salary; //Поле может быть null, Значение поля должно быть больше 0
    private java.util.Date startDate; //Поле не может быть null
    private java.time.ZonedDateTime endDate; //Поле может быть null
    private Status status; //Поле не может быть null
    private Organization organization; //Поле может быть null
}
