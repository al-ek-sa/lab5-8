package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganizationType {
    private InputReader scanner;
    public ValidationOrganizationType(){
        this.scanner = InputReader.getInstance();
    }

    public OrganizationType organizationType(){
        System.out.println("Organization type options:");
        for (OrganizationType type : OrganizationType.values()){
            System.out.println(type);
        }
        String organizationTypeConsole = scanner.nextLine();
        for (OrganizationType nameObject : OrganizationType.values()){
            if(nameObject.name().equals(organizationTypeConsole)){
                return nameObject;
            }
        }
        System.out.println("Invalid input, please enter the value again");
        return organizationType();
    }
}
