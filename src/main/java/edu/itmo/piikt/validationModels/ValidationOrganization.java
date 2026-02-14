package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganization {
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private InputReader scanner;
    public ValidationOrganization(){
        this.scanner = InputReader.getInstance();
        this.type = new ValidationOrganizationType();
        this.address = new ValidationAddress();
    }

    public Address validationNullAddress(){
        System.out.println("Введите адрес");
        Address addressConsole = address.validationAddress();
        if (addressConsole != null ){
            return addressConsole;
        } System.out.println("Адрес не введен неверно");
        return validationNullAddress();
    }

    public OrganizationType validationNullOrganizationType(){
        System.out.println("Выберете тип организации");
        OrganizationType organizationTypeConsole = type.validationOrganizationType();
        if (organizationTypeConsole != null ) {
            return organizationTypeConsole;
        } return validationNullOrganizationType();
    }

    public int validationAnnualTurnover(){
        System.out.println("Введите годовой доход");
        int annualTurnoverConsole = scanner.nextInt();
        if (annualTurnoverConsole > 0) {
            return annualTurnoverConsole;
        } return  validationAnnualTurnover();
    }

    public Organization validationOrganization(){
        return new Organization(validationAnnualTurnover(), validationNullOrganizationType(), validationNullAddress());
    }
}
