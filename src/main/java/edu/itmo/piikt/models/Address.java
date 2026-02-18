package edu.itmo.piikt.models;

import com.opencsv.bean.CsvBindByPosition;

import java.util.Objects;

public class Address {
    @CsvBindByPosition(position = 11)
     private String street; //Поле не может быть null
     public Address(String street){
     this.street = street;
     }

    @Override
    public String toString() {
        return " улица: " + street;
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