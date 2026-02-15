package edu.itmo.piikt.models;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

import java.util.Objects;

final public class Organization {
    @CsvBindByPosition(position = 10)
    private int annualTurnover; //Значение поля должно быть больше 0
    @CsvBindByPosition(position =11)
    private OrganizationType type; //Поле не может быть null
    @CsvRecurse
    private Address officialAddress; //Поле не может быть null

    public Organization(int annualTurnover, OrganizationType type, Address officialAddress) {
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    @Override
    public String toString() {
        return " годовой оборот: " + annualTurnover + ", тип: " + type.toString() + ", адрес: " + officialAddress.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(annualTurnover, type, officialAddress);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() !=obj.getClass()) return  false;
        Organization organization = (Organization) obj;
        return annualTurnover == organization.annualTurnover && Objects.equals(officialAddress, organization.officialAddress)
                && type == organization.type;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    public int getAnnualTurnover() {
        return annualTurnover;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public void setAnnualTurnover(int annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }
}
