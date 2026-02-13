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
        if (address.validationAddress() != null ){
            return address.validationAddress();
        } return validationNullAddress();
    }

    public OrganizationType validationNullOrganizationType(){
        if (type.validationOrganizationType() != null ) {
            return type.validationOrganizationType();
        } return validationNullOrganizationType();
    }

    public int validationAnnualTurnover(){
        int annualTurnoverConsole = scanner.nextInt();
        if (annualTurnoverConsole > 0) {
            return annualTurnoverConsole;
        } return  validationAnnualTurnover();
    }

    public Organization validationOrganization(){
        return new Organization(validationAnnualTurnover(), validationNullOrganizationType(), validationNullAddress());
    }
}
