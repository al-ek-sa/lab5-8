package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganization {
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private IOProvider io;
    public ValidationOrganization(IOProvider io){
        this.type = new ValidationOrganizationType(io);
        this.address = new ValidationAddress(io);
        this.io = io;
    }

    public Address validationNullAddress(){
        System.out.println("Enter the address");
        while (true){
            io.println("Enter the address");
            Address addressConsole = address.validationAddress();
            if (addressConsole != null ){
                return addressConsole;
            } else System.out.println("Required field");
            System.out.println("Enter the address");
        }
    }

    public OrganizationType validationNullOrganizationType(){
        System.out.println("Select the organization type from the suggested options and enter it in uppercase");
        OrganizationType organizationTypeConsole = type.organizationType();
        if (organizationTypeConsole != null ) {
            return organizationTypeConsole;
        } return validationNullOrganizationType();
    }

    public int validationAnnualTurnover(){
        System.out.println("Enter annual turnover (annual turnover must be an integer greater than 0)");
        while (true) {
            try{
                String input = io.readLIne();
                int annualTurnoverConsole = Integer.parseInt(input);
                if (annualTurnoverConsole > 0) {
                    return annualTurnoverConsole;
                } else System.out.println("Invalid input, please enter the value again");
                System.out.println("Enter annual turnover (annual turnover must be an integer greater than 0)");
        } catch (RuntimeException e) {
                System.out.println("Invalid input, please enter the value again");
                System.out.println("Enter annual turnover (annual turnover must be an integer greater than 0)");
            }}
    }

    public Organization organization(){
        return new Organization(validationAnnualTurnover(), validationNullOrganizationType(), validationNullAddress());
    }
}
