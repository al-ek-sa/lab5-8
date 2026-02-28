package edu.itmo.piikt.models;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

import java.util.Objects;

/**
 * The class of the Organization type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

final public class Organization {
    @CsvBindByPosition(position = 9)
    private int annualTurnover;
    @CsvBindByPosition(position = 10)
    private OrganizationType type;
    @CsvRecurse
    private Address officialAddress;

    public Organization(int annualTurnover, OrganizationType type, Address officialAddress) {
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }
    public Organization(){}

    /**
     * Returns a brief description of this Organization. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "annualTurnover: annualTurnover, type: type, officialAddress: officialAddress"
     */

    @Override
    public String toString() {
        return " annualTurnover: " + annualTurnover + ", type: " + type.toString() + ", officialAddress: " + officialAddress.toString();
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
