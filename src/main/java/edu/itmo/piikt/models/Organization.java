package edu.itmo.piikt.models;

public class Organization {
    private int annualTurnover; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    private Address officialAddress; //Поле не может быть null

    public Organization(int annualTurnover, OrganizationType type, Address officialAddress) {
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }
}
