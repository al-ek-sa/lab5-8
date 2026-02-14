package edu.itmo.piikt.models;

public record Address(String street){
    @Override
    public String toString() {
        return " улица: " + street;
    }
}

/**public class Address {
 private String street; //Поле не может быть null
 public Address(String street){
 this.stree = street;
 }
 }*/