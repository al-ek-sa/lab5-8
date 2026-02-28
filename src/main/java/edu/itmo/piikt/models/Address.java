package edu.itmo.piikt.models;

import com.opencsv.bean.CsvBindByPosition;
import jdk.jfr.DataAmount;

import java.util.Objects;

/**
 * The class of the Address type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class Address {
    @CsvBindByPosition(position = 11)
     private String street;
     public Address(String street){
     this.street = street;
     }
     public Address() {}

/**
 * Returns a brief description of this Address. The exact details
 * of the representation are unspecified and subject to change,
 * but the following may be regarded as typical:
 *
 * "street: street"
 */

    @Override
    public String toString() {
        return " street: " + street;
    }

    @Override
    public int hashCode() {
        return Objects.hash(street);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return Objects.equals(street, address.street);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}