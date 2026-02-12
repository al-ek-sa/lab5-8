package edu.itmo.piikt.models;

public class Address {
    private String street; //Поле не может быть null
    public Address(String street) {
        this.street = validationStreet();
    }

    private String validationStreet(){
       if (street != null) {
            System.out.println("Необходимо ввести улицу");
        } return street;
    }
}
