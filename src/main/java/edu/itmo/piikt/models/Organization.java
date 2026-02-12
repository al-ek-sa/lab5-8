package edu.itmo.piikt.models;

public class Organization {
    private int annualTurnover; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    private Address officialAddress; //Поле не может быть null

    public Organization(int annualTurnover, Organization type, Address officialAddress) {
        this.annualTurnover = validationAnnualTurnover();
        this.type = validationType();
        this.officialAddress = validationOfficialAddress();
    }

    private int validationAnnualTurnover(){
        if (annualTurnover<=0) {
            System.out.println("Значение меньше либо равно нулю: = " + annualTurnover);
        } return annualTurnover;
    }

    private OrganizationType validationType(){
        if (type == null) {
            System.out.println("значение null");
        } return  type;
    }

    private Address validationOfficialAddress(){
        if (officialAddress == null){
            System.out.println("значение null");
        } return officialAddress;
    }
}
